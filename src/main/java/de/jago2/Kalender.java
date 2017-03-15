package de.jago2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.GregorianCalendar;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;
import net.fortuna.ical4j.validate.ValidationException;

public class Kalender {

	UidGenerator generator;

	public Kalender() {
		try {
			generator = new UidGenerator("1");
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void addEventToCalendar(Calendar calendar, Date date, String summary) {
		VEvent event = new VEvent(date, summary);
		event.getProperties().add(generator.generateUid());
		calendar.getComponents().add(event);
	}

	public void createICS() throws ValidationException, IOException {
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//jago//iCal4j 2.0.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);

		java.util.Calendar calendar2 = new GregorianCalendar();
		
		// ET setzen
		calendar2.set(java.util.Calendar.MONTH, java.util.Calendar.JULY);
		calendar2.set(java.util.Calendar.DAY_OF_MONTH, 28);
		
		addEventToCalendar(calendar, new Date(calendar2.getTime()), "40+0");
		for (int i = 0; i < 40 * 7; i++) {
			calendar2.add(java.util.Calendar.DAY_OF_MONTH, -1);
			addEventToCalendar(calendar, new Date(calendar2.getTime()), (40 - 1) - (i / 7) + "+" + (6 - (i % 7)));
		}
		FileOutputStream outputStream = new FileOutputStream("cal.ics");
		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, outputStream);
	}

	public static void main(String[] args) throws ValidationException, IOException {
		new Kalender().createICS();
	}

}
