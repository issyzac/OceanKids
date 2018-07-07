package apps.issy.com.oceankids;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Created by issy on 04/03/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project OceanKids
 */

public class JavaDateParser {

    private long dateInLong;
    private String dateInString;
    private Date dateInDate;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");


    public JavaDateParser(){
        dateInDate = new Date();
    }

    public void convertDate(long longDate){
        this.dateInLong = longDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(longDate);

        dateInDate = calendar.getTime();
        dateInString = simpleDateFormat.format(dateInDate);
    }

    public int getAge(Date date){

        Date now = new Date();

        Calendar a = Calendar.getInstance();
        a.setTime(date);

        Calendar b = Calendar.getInstance();
        b.setTime(now);

        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public long getDateInLong() {
        return dateInLong;
    }

    public void setDateInLong(long dateInLong) {
        this.dateInLong = dateInLong;
    }

    public String getDateInString() {
        return dateInString;
    }

    public void setDateInString(String dateInString) {
        this.dateInString = dateInString;
    }

    public Date getDateInDate() {
        return dateInDate;
    }

    public void setDateInDate(Date dateInDate) {
        this.dateInDate = dateInDate;
    }
}
