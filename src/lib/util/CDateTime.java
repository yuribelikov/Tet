package lib.util;

import java.util.Calendar;
import java.util.Date;

public class CDateTime
{
  public static final int DATE = 1;
  public static final int CALENDAR = 2;
  public static final int STRING = 3;

  protected int pos_year[];
  protected int pos_month[];
  protected int pos_day[];
  protected int pos_hour[];
  protected int pos_minute[];
  protected int pos_second[];
  //--------------------------------SQL
  public static final int YEAR = 1;
  public static final int MONTH = 2;
  public static final int DAY = 3;
  public static final int HOUR = 4;
  public static final int MINUTE = 5;
  public static final int SECOND = 6;

  protected String SQL;
  protected String NameField;

  /**
   * CDateTime constructor comment.
   */
  public CDateTime()
  {
    setFormat();
  }

  protected String addChar(String str, int need_char)
  {
    if (str.length() < need_char)
    {
      for (int i = 0; i < (need_char - str.length() + 1); i++) str = "0" + str;
    }
    return str;
  }

  public void addWhere(int _what, int _fixpos)
  {
    int length = 0;
    if (SQL.length() != 0) SQL += " AND (";
    else SQL = "(";

    SQL += " substr(" + NameField + ",";
    switch (_what)
    {
      case YEAR:
      {
        SQL += Integer.toString(pos_year[0] + 1) + "," + Integer.toString(pos_year[1] - pos_year[0] + 1) + ")";
        length = (pos_year[1] - pos_year[0]);
        break;
      }
      case MONTH:
      {
        SQL += Integer.toString(pos_month[0] + 1) + "," + Integer.toString(pos_month[1] - pos_month[0] + 1) + ")";
        length = (pos_month[1] - pos_month[0]);
        break;
      }
      case DAY:
      {
        SQL += Integer.toString(pos_day[0] + 1) + "," + Integer.toString(pos_day[1] - pos_day[0] + 1) + ")";
        length = (pos_day[1] - pos_day[0]);
        break;
      }
      case HOUR:
      {
        SQL += Integer.toString(pos_hour[0] + 1) + "," + Integer.toString(pos_hour[1] - pos_hour[0] + 1) + ")";
        length = (pos_hour[1] - pos_hour[0]);
        break;
      }
      case MINUTE:
      {
        SQL += Integer.toString(pos_minute[0] + 1) + "," + Integer.toString(pos_minute[1] - pos_minute[0] + 1) + ")";
        length = (pos_minute[1] - pos_minute[0]);
        break;
      }
      case SECOND:
      {
        SQL += Integer.toString(pos_second[0] + 1) + "," + Integer.toString(pos_second[1] - pos_second[0] + 1) + ")";
        length = (pos_second[1] - pos_second[0]);
        break;
      }
    }
    length++;

    SQL += "='" + addChar(Integer.toString(_fixpos), length) + "'";
    SQL += " )";
  }

  public void addWhere(int _what, int _begin, int _end)
  {
    int length = 0;
    if (SQL.length() != 0) SQL += " AND (";
    else SQL = "(";

    SQL += " substr(" + NameField + ",";
    switch (_what)
    {
      case YEAR:
      {
        SQL += Integer.toString(pos_year[0] + 1) + "," + Integer.toString(pos_year[1] - pos_year[0] + 1) + ")";
        length = (pos_year[1] - pos_year[0]);
        break;
      }
      case MONTH:
      {
        SQL += Integer.toString(pos_month[0] + 1) + "," + Integer.toString(pos_month[1] - pos_month[0] + 1) + ")";
        length = (pos_month[1] - pos_month[0]);
        break;
      }
      case DAY:
      {
        SQL += Integer.toString(pos_day[0] + 1) + "," + Integer.toString(pos_day[1] - pos_day[0] + 1) + ")";
        length = (pos_day[1] - pos_day[0]);
        break;
      }
      case HOUR:
      {
        SQL += Integer.toString(pos_hour[0] + 1) + "," + Integer.toString(pos_hour[1] - pos_hour[0] + 1) + ")";
        length = (pos_hour[1] - pos_hour[0]);
        break;
      }
      case MINUTE:
      {
        SQL += Integer.toString(pos_minute[0] + 1) + "," + Integer.toString(pos_minute[1] - pos_minute[0] + 1) + ")";
        length = (pos_minute[1] - pos_minute[0]);
        break;
      }
      case SECOND:
      {
        SQL += Integer.toString(pos_second[0] + 1) + "," + Integer.toString(pos_second[1] - pos_second[0] + 1) + ")";
        length = (pos_second[1] - pos_second[0]);
        break;
      }
    }
    length++;

    SQL += " between '" + addChar(Integer.toString(_begin), length) + "' and '" + addChar(Integer.toString(_end), length) + "'";
    SQL += " )";
  }

  public Object getDateTime(int return_type, String _date)
  {
    int year = 0, month = 0, day = 0, hour = 0, minute = 0, second = 0;

    try
    {
      Calendar calendar = Calendar.getInstance();

      year = Integer.parseInt(_date.substring(pos_year[0], pos_year[1] + 1));
      month = Integer.parseInt(_date.substring(pos_month[0], pos_month[1] + 1)) - 1;
      day = Integer.parseInt(_date.substring(pos_day[0], pos_day[1] + 1));
      hour = Integer.parseInt(_date.substring(pos_hour[0], pos_hour[1] + 1));
      minute = Integer.parseInt(_date.substring(pos_minute[0], pos_minute[1] + 1));
      second = Integer.parseInt(_date.substring(pos_second[0], pos_second[1] + 1));

      calendar.set(year, month, day, hour, minute, second);

      calendar.add(Calendar.MILLISECOND, calendar.get(calendar.ZONE_OFFSET) + calendar.get(calendar.DST_OFFSET));

      switch (return_type)
      {
        case STRING:
        {
          java.text.SimpleDateFormat sDate = new java.text.SimpleDateFormat();
          return sDate.format(calendar.getTime());
        }
        case CALENDAR:
        {
          return calendar;
        }
        case DATE:
        {
          return calendar.getTime();
        }
      }

      return calendar;
    }
    catch (Exception e)
    {
      return "error format";
    }
  }

  public String getSQL()
  {
    return SQL;
  }

  public String getSQL(String _nameField, String _begin_time, String _last_time)
  {
    return " (" + _nameField + " " + "between '" + _begin_time + "' AND '" + _last_time + "' )";
  }

  public String getSQL(String _columnName, Date _begin_date, Date _end_date)
  {
    return getSQL(_columnName, getStandartString(DATE, _begin_date), getStandartString(DATE, _end_date));
  }

  public String getStandartString(int _type, Object obj)
  {
    String ret_str = "";
    Calendar calendar = Calendar.getInstance();
    switch (_type)
    {
      case STRING:
      {
        break;
      }
      case DATE:
      {
        calendar.setTime((Date) obj);
        break;
      }
      case CALENDAR:
      {
        calendar = (Calendar) obj;
        break;
      }
    }
    calendar.add(Calendar.MILLISECOND, -calendar.get(calendar.ZONE_OFFSET) - calendar.get(calendar.DST_OFFSET));
    String s_year = addChar(Integer.toString(calendar.get(Calendar.YEAR)), 4);
    String s_month = addChar(Integer.toString(calendar.get(Calendar.MONTH) + 1), 2);
    String s_day = addChar(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)), 2);
    String s_hour = addChar(Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)), 2);
    String s_minute = addChar(Integer.toString(calendar.get(Calendar.MINUTE)), 2);
    String s_second = addChar(Integer.toString(calendar.get(Calendar.SECOND)), 2);


    return s_year + '-' + s_month + '-' + s_day + ' ' + s_hour + ':' + s_minute + ':' + s_second;
  }

  protected void setFormat()
  {
    pos_year = new int[2];
    pos_hour = new int[2];
    pos_minute = new int[2];
    pos_month = new int[2];
    pos_second = new int[2];
    pos_day = new int[2];

    pos_year[0] = 0;
    pos_year[1] = 3;
    pos_month[0] = 5;
    pos_month[1] = 6;
    pos_day[0] = 8;
    pos_day[1] = 9;
    pos_hour[0] = 11;
    pos_hour[1] = 12;
    pos_minute[0] = 14;
    pos_minute[1] = 15;
    pos_second[0] = 17;
    pos_second[1] = 18;
  }

  public void setFormat(int _year1, int _year2, int _month1, int _month2, int _day1, int _day2, int _hour1, int _hour2, int _minute1, int _minute2, int _second1, int _second2)
  {
    pos_year = new int[2];
    pos_hour = new int[2];
    pos_minute = new int[2];
    pos_month = new int[2];
    pos_second = new int[2];
    pos_day = new int[2];

    pos_year[0] = _year1;
    pos_year[1] = _year2;
    pos_month[0] = _month1;
    pos_month[1] = _month2;
    pos_day[0] = _day1;
    pos_day[1] = _day2;
    pos_hour[0] = _hour1;
    pos_hour[1] = _hour2;
    pos_minute[0] = _minute1;
    pos_minute[1] = _minute2;
    pos_second[0] = _second1;
    pos_second[1] = _second2;
  }

  public void startMakeSQL(String _nameField)
  {
    SQL = "";
    NameField = _nameField;
  }
}