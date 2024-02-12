import datetime
import numpy as np
from sklearn.linear_model import LinearRegression
import requests
import calendar
import traceback
import sys

app_url = "http://localhost:8080"

def get_payment_dates(jwt_token):
    try:
        headers = {"Authorization": f"{jwt_token}"}
        response = requests.get(f"{app_url}/api/bills", headers=headers)
        response.raise_for_status()

        payment_data = response.json()

        payment_dates = []
        for date in payment_data:
            try:
                if isinstance(date['dueDate'], list):
                    # Convert List to String with format 'yyyy-mm-dd'
                    date_str = '-'.join(map(str, date['dueDate']))
                    payment_dates.append(datetime.datetime.strptime(date_str, '%Y-%m-%d'))
                else:
                    payment_dates.append(datetime.datetime.strptime(date['dueDate'], '%Y-%m-%d'))
            except ValueError as ve:
                print(f"Error parsing date: {ve} - Skipping date: {date['dueDate']}")
        return payment_dates
    except requests.exceptions.RequestException:
        print("Error fetching payment dates")
        return []
    except requests.exceptions.HTTPError as errh:
        print(f"HTTP Error: {errh}")
        print(f"Response content: {response.content.decode('utf-8')}")
        return []
    except requests.exceptions.ConnectionError as errc:
        print(f"Error Connecting: {errc}")
        return []
    except requests.exceptions.Timeout as errt:
        print(f"Timeout Error: {errt}")
        return []
    except requests.exceptions.RequestException as err:
        print(f"Request Exception: {err}")
        return []
    except Exception as e:
        print(f"Unexpected Error: {e}")
        return []



def generate_optimal_reminder_date(payment_dates):
    if len(payment_dates) == 0:
        return datetime.datetime.today()  # Use today's date and time if there are no previous payments

    days_since_start = np.array([(date - payment_dates[0]).days for date in payment_dates]).reshape(-1, 1)
    days_of_month = np.array([date.day for date in payment_dates])
    days_since_start_2d = days_since_start.reshape(-1, 1)

    model = LinearRegression()
    model.fit(days_since_start_2d, days_of_month)

    days_since_start_last_payment = days_since_start[-1, 0]
    days_since_start_next_month = days_since_start_last_payment + 30
    optimal_day_next_month = model.predict([[days_since_start_next_month]])[0]

    last_payment_date = payment_dates[-1]
    optimal_reminder_month = last_payment_date.month + 1
    optimal_reminder_year = last_payment_date.year + (optimal_reminder_month // 13)
    optimal_reminder_month %= 12
    if optimal_reminder_month == 0:
        optimal_reminder_month = 12

    # Handle special case for February
    if optimal_reminder_month == 2:
        # Check if the predicted day is 30 or 31
        if optimal_day_next_month > 28:
            # If the year is a leap year, set to February 29, otherwise, set to February 28
            if (optimal_reminder_year % 4 == 0 and optimal_reminder_year % 100 != 0) or (optimal_reminder_year % 400 == 0):
                optimal_day_next_month = 29
            else:
                optimal_day_next_month = 28

    # Handle months with 30 and 31 days
    if optimal_day_next_month > calendar.monthrange(optimal_reminder_year, optimal_reminder_month)[1]:
        optimal_day_next_month = calendar.monthrange(optimal_reminder_year, optimal_reminder_month)[1]

    optimal_reminder_date = datetime.datetime(optimal_reminder_year, optimal_reminder_month, int(optimal_day_next_month))

    try:
        # Print the optimal reminder date without any error messages
        print(optimal_reminder_date.strftime('%Y-%m-%d %H:%M:%S'))
    except Exception as e:
        # Print the error message in case of an exception
        print(f"Error in script output: {e}")

if __name__ == '__main__':
    if len(sys.argv) < 4:
        print("Usage: python3 notification_script.py <username> <due_date_string> <authorization_header>")
        sys.exit(1)

    username = sys.argv[1]
    due_date_string = sys.argv[2]
    authorization_header = sys.argv[3]


    try:
        payment_dates = get_payment_dates(authorization_header)  # Pass authorization header to get_payment_dates
        if payment_dates:
            generate_optimal_reminder_date(payment_dates)
    except Exception as e:
        print(f"Error: {e}")

