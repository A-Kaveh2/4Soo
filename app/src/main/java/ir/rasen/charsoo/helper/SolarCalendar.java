package ir.rasen.charsoo.helper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import ir.rasen.charsoo.R;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

public class SolarCalendar {

	private String strWeekDay = "";
	private String strMonth = "";
	private static int date;
	private static int month;
	private static int year;
	private int weekDay;
	private int next_month_first_day = -1;
	private int last_month_last_day = -1;
	static int temp_year;
	static int temp_month;
	Context activity;
	ArrayList<String> month_name_list = new ArrayList<String>();
	ArrayList<String> week_days_name_list = new ArrayList<String>();

	public String get_week_day_str() {
		return strWeekDay;

	}

	public String get_month_str() {
		return strMonth;

	}

	public int get_date() {
		return date;

	}

	public int get_month() {
		return month;

	}

	public int get_year() {
		return year;

	}

	public int get_week_day() {
		return weekDay;

	}

	public static int get_temp_month() {
		return temp_month;
	}

	public static int get_temp_year() {
		return temp_year;
	}

	public int get_week_day_solar() {
		Calendar c = Calendar.getInstance();
		int day = 0;
		day = c.get(Calendar.DAY_OF_WEEK);
		switch (c.get(Calendar.DAY_OF_WEEK)) {

		case 1:
			day = 2;
			break;
		case 2:
			day = 3;
			break;
		case 3:
			day = 4;
			break;
		case 4:
			day = 5;
			break;
		case 5:
			day = 6;
			break;
		case 6:
			day = 7;
			break;
		case 7:
			day = 1;
			break;
		}
		return day;
	}

	public SolarCalendar(Context activity) {
		Calendar c = Calendar.getInstance();
		month_name_list.add(activity.getResources().getString(
				R.string.Farvardin));
		month_name_list.add(activity.getResources().getString(
				R.string.Ordibehesht));
		month_name_list
				.add(activity.getResources().getString(R.string.Khordad));
		month_name_list.add(activity.getResources().getString(R.string.Tir));
		month_name_list.add(activity.getResources().getString(R.string.Mordad));
		month_name_list.add(activity.getResources().getString(
				R.string.Shahrivar));
		month_name_list.add(activity.getResources().getString(R.string.Mehr));
		month_name_list.add(activity.getResources().getString(R.string.Aban));
		month_name_list.add(activity.getResources().getString(R.string.Azar));
		month_name_list.add(activity.getResources().getString(R.string.Dey));
		month_name_list.add(activity.getResources().getString(R.string.Bahman));
		month_name_list.add(activity.getResources().getString(R.string.Esfand));

		week_days_name_list
				.add(activity.getResources().getString(R.string.Sat));
		week_days_name_list
				.add(activity.getResources().getString(R.string.Sun));
		week_days_name_list
				.add(activity.getResources().getString(R.string.Mon));
		week_days_name_list
				.add(activity.getResources().getString(R.string.Tue));
		week_days_name_list
				.add(activity.getResources().getString(R.string.Wed));
		week_days_name_list
				.add(activity.getResources().getString(R.string.Thr));
		week_days_name_list
				.add(activity.getResources().getString(R.string.Fri));
		calcSolarCalendar(c);
		this.activity = activity;

	}

	// public SolarCalendar(Date MiladiDate) { calcSolarCalendar(MiladiDate); }

	private void calcSolarCalendar(Calendar c) {

		int ld;
		// Calendar c = Calendar.getInstance();

		int miladiYear = c.get(Calendar.YEAR);
		int miladiMonth = c.get(Calendar.MONTH) + 1;
		int miladiDate = c.get(Calendar.DATE);
		int WeekDay = c.get(Calendar.DAY_OF_WEEK);
		weekDay = WeekDay;
		int[] buf1 = new int[12];
		int[] buf2 = new int[12];

		buf1[0] = 0;
		buf1[1] = 31;
		buf1[2] = 59;
		buf1[3] = 90;
		buf1[4] = 120;
		buf1[5] = 151;
		buf1[6] = 181;
		buf1[7] = 212;
		buf1[8] = 243;
		buf1[9] = 273;
		buf1[10] = 304;
		buf1[11] = 334;

		buf2[0] = 0;
		buf2[1] = 31;
		buf2[2] = 60;
		buf2[3] = 91;
		buf2[4] = 121;
		buf2[5] = 152;
		buf2[6] = 182;
		buf2[7] = 213;
		buf2[8] = 244;
		buf2[9] = 274;
		buf2[10] = 305;
		buf2[11] = 335;

		if ((miladiYear % 4) != 0) {
			date = buf1[miladiMonth - 1] + miladiDate;

			if (date > 79) {
				date = date - 79;
				if (date <= 186) {
					switch (date % 31) {
					case 0:
						month = date / 31;
						date = 31;
						break;
					default:
						month = (date / 31) + 1;
						date = (date % 31);
						break;
					}
					year = miladiYear - 621;
				} else {
					date = date - 186;

					switch (date % 30) {
					case 0:
						month = (date / 30) + 6;
						date = 30;
						break;
					default:
						month = (date / 30) + 7;
						date = (date % 30);
						break;
					}
					year = miladiYear - 621;
				}
			} else {
				if ((miladiYear > 1996) && (miladiYear % 4) == 1) {
					ld = 11;
				} else {
					ld = 10;
				}
				date = date + ld;

				switch (date % 30) {
				case 0:
					month = (date / 30) + 9;
					date = 30;
					break;
				default:
					month = (date / 30) + 10;
					date = (date % 30);
					break;
				}
				year = miladiYear - 622;
			}
		} else {
			date = buf2[miladiMonth - 1] + miladiDate;

			if (miladiYear >= 1996) {
				ld = 79;
			} else {
				ld = 80;
			}
			if (date > ld) {
				date = date - ld;

				if (date <= 186) {
					switch (date % 31) {
					case 0:
						month = (date / 31);
						date = 31;
						break;
					default:
						month = (date / 31) + 1;
						date = (date % 31);
						break;
					}
					year = miladiYear - 621;
				} else {
					date = date - 186;

					switch (date % 30) {
					case 0:
						month = (date / 30) + 6;
						date = 30;
						break;
					default:
						month = (date / 30) + 7;
						date = (date % 30);
						break;
					}
					year = miladiYear - 621;
				}
			}

			else {
				date = date + 10;

				switch (date % 30) {
				case 0:
					month = (date / 30) + 9;
					date = 30;
					break;
				default:
					month = (date / 30) + 10;
					date = (date % 30);
					break;
				}
				year = miladiYear - 622;
			}

		}

		switch (month) {
		case 1:
			strMonth = month_name_list.get(0);
			break;
		case 2:
			strMonth = month_name_list.get(1);
			break;
		case 3:
			strMonth = month_name_list.get(2);
			break;
		case 4:
			strMonth = month_name_list.get(3);
			break;
		case 5:
			strMonth = month_name_list.get(4);
			break;
		case 6:
			strMonth = month_name_list.get(5);
			break;
		case 7:
			strMonth = month_name_list.get(6);
			break;
		case 8:
			strMonth = month_name_list.get(7);
			break;
		case 9:
			strMonth = month_name_list.get(8);
			break;
		case 10:
			strMonth = month_name_list.get(9);
			break;
		case 11:
			strMonth = month_name_list.get(10);
			break;
		case 12:
			strMonth = month_name_list.get(11);
			break;
		}

		switch (WeekDay) {

		case Calendar.SUNDAY:
			strWeekDay = week_days_name_list.get(1);
			break;
		case  Calendar.MONDAY:
			strWeekDay = week_days_name_list.get(2);
			break;
		case  Calendar.TUESDAY:
			strWeekDay = week_days_name_list.get(3);
			break;
		case  Calendar.WEDNESDAY:
			strWeekDay = week_days_name_list.get(4);
			break;
		case  Calendar.THURSDAY:
			strWeekDay = week_days_name_list.get(5);
			break;
		case  Calendar.FRIDAY:
			strWeekDay = week_days_name_list.get(6);
			break;
		case  Calendar.SATURDAY:
			strWeekDay = week_days_name_list.get(0);
			break;
		}
		temp_month = month;
		temp_year = year;
	}

	public String getMonthString(int month) {
		String strMonth = "";
		switch (month) {
		case 1:
			strMonth = month_name_list.get(0);
			break;
		case 2:
			strMonth = month_name_list.get(1);
			break;
		case 3:
			strMonth = month_name_list.get(2);
			break;
		case 4:
			strMonth = month_name_list.get(3);
			break;
		case 5:
			strMonth = month_name_list.get(4);
			break;
		case 6:
			strMonth = month_name_list.get(5);
			break;
		case 7:
			strMonth = month_name_list.get(6);
			break;
		case 8:
			strMonth = month_name_list.get(7);
			break;
		case 9:
			strMonth = month_name_list.get(8);
			break;
		case 10:
			strMonth = month_name_list.get(9);
			break;
		case 11:
			strMonth = month_name_list.get(10);
			break;
		case 12:
			strMonth = month_name_list.get(11);
			break;
		}

		return strMonth;
	}

	/*private String weekDayString(int weekDay) {
		String strWeekDay = "";
		switch (weekDay) {

		case 0:
			strWeekDay = week_days_name_list.get(1);
			break;
		case 1:
			strWeekDay = week_days_name_list.get(2);
			break;
		case 2:
			strWeekDay = week_days_name_list.get(3);
			break;
		case 3:
			strWeekDay = week_days_name_list.get(4);
			break;
		case 4:
			strWeekDay = week_days_name_list.get(5);
			break;
		case 5:
			strWeekDay = week_days_name_list.get(6);
			break;
		case 6:
			strWeekDay = week_days_name_list.get(0);
			break;
		}
		return strWeekDay;
	}*/

	public String getCurrentShamsidate() {

		String str = strWeekDay + " " + date + " " + strMonth + " سال "
				+ String.valueOf(year);

		return str;
	}

	private SolarCalendar getSolarDate(Activity activity) {

		return new SolarCalendar(activity);

	}

	public static int month_length(int month,int year) {
		boolean second_half_year = false;
		if (month > 6)
			second_half_year = true;
		int days_limit = -1;
		if (second_half_year)
			days_limit = 30;
		else
			days_limit = 31;
		if (month == 12) {
			if (year % 4 == 3)
				days_limit = 30;// kabiseh year
			else
				days_limit = 29;
		}
		return days_limit;
	}

	public void set_year(int new_year) {
		this.year = new_year;
	}

	public static boolean curren_day(int day) {
		if (day == date && temp_month == month && temp_year == year)
			return true;
		return false;
	}

	private int[] get_month_days(TextView txt_month_str, int selected_month,
			int move) {// move=0 : current month,move=1:
						// next month, move=-1: last month

		txt_month_str.setText(getMonthString(selected_month) + " " + temp_year);

		int first_day = 0;// first day of month in week
		int dd;

		if (move == 1) {
			first_day = next_month_first_day;

		} else if (move == 0) {
			dd = date - get_week_day_solar();
			if (dd == 0)
				first_day = 1;
			else if (dd > 0)
				first_day = 8 - (dd % 7);
			else if (dd < 0)
				first_day = (dd * -1) + 1;

		} else if (move == -1) {
			dd = month_length(selected_month,temp_year) - last_month_last_day;// like date
																	// -
																	// get_week_day_solar();.
			// month_lenth(selected_month) is suppose to be selected date
			// last_month_last_day is day in week of selected date
			if (dd == 0)
				first_day = 1;
			else if (dd > 0)
				first_day = 8 - (dd % 7);
			else if (dd < 0)
				first_day = (dd * -1) + 1;
		}

		return calculate_days(first_day, selected_month);
	}

	private int[] calculate_days(int first_day_in_week, int selected_month) {
		int counter = 1;
		int array[][] = new int[6][7];
		last_month_last_day = first_day_in_week - 1;
		if (last_month_last_day == 0)
			last_month_last_day = 7;

		int month_length = month_length(selected_month,temp_year);
		for (int row = 0; row < 6; row++) {
			for (int column = 0; column < 7; column++) {
				if (row == 0 && column < first_day_in_week - 1)// first_day-1
																// because row
																// starts from 0
					continue;
				if (counter > month_length) {
					next_month_first_day = column + 1;
					row = 7;
					column = 8;
					continue;
				}
				array[row][column] = counter;
				counter++;

			}
		}

		int array_final[] = new int[42];
		int counter_final = 0;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				array_final[counter_final++] = array[i][j];
			}

		}

		return array_final;

	}

	public String get_solar_month(int month) {
		
		switch (month) {
		case 1:
			strMonth = month_name_list.get(0);
			break;
		case 2:
			strMonth = month_name_list.get(1);
			break;
		case 3:
			strMonth = month_name_list.get(2);
			break;
		case 4:
			strMonth = month_name_list.get(3);
			break;
		case 5:
			strMonth = month_name_list.get(4);
			break;
		case 6:
			strMonth = month_name_list.get(5);
			break;
		case 7:
			strMonth = month_name_list.get(6);
			break;
		case 8:
			strMonth = month_name_list.get(7);
			break;
		case 9:
			strMonth = month_name_list.get(8);
			break;
		case 10:
			strMonth = month_name_list.get(9);
			break;
		case 11:
			strMonth = month_name_list.get(10);
			break;
		case 12:
			strMonth = month_name_list.get(11);
			break;
		}
		return strMonth;
	}

	public int[] get_next_month(TextView txt_month) {
		SolarCalendar.temp_month++;
		if (SolarCalendar.temp_month > 12) {
			SolarCalendar.temp_month = 1;
			SolarCalendar.temp_year++;
		}
		return get_month_days(txt_month, SolarCalendar.temp_month, 1);
	}

	public int[] get_last_month(TextView txt_month) {
		SolarCalendar.temp_month--;
		if (SolarCalendar.temp_month < 1) {
			SolarCalendar.temp_month = 12;
			SolarCalendar.temp_year--;
		}
		return get_month_days(txt_month, SolarCalendar.temp_month, -1);
	}

	public int[] get_current_month(TextView txt_month) {
		return get_month_days(txt_month, get_month(), 0);
	}
}
