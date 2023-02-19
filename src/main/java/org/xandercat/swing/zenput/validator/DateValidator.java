package org.xandercat.swing.zenput.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xandercat.swing.zenput.annotation.ValidateDate;
import org.xandercat.swing.zenput.error.ValidationException;

public class DateValidator extends AbstractValidator<Date> {

	private DateFormat dateFormat;
	private Date before;
	private Date after;
	private boolean beforeInclusive;
	private boolean afterInclusive;
	
	public static DateValidator newValidator(ValidateDate annotation) throws ParseException {
		DateValidator validator =  new DateValidator(annotation.pattern());
		if (annotation.before() != null) {
			validator.setBefore(annotation.before(), annotation.beforeInclusive());
		}
		if (annotation.after() != null) {
			validator.setAfter(annotation.after(), annotation.afterInclusive());
		}
		return validator;
	}
	
	public DateValidator(String pattern) {
		super();
		this.dateFormat = new SimpleDateFormat(pattern);
	}
	
	public void setBefore(String dateString, boolean inclusive) throws ParseException {
		setBefore(this.dateFormat.parse(dateString), inclusive);
	}
	
	public void setBefore(Date date, boolean inclusive) {
		this.before = date;
		this.beforeInclusive = inclusive;
	}
	
	public void setAfter(String dateString, boolean inclusive) throws ParseException {
		setAfter(this.dateFormat.parse(dateString), inclusive);
	}
	
	public void setAfter(Date date, boolean inclusive) {
		this.after = date;
		this.afterInclusive = inclusive;
	}
	
	@Override
	public Class<Date> getValueType() {
		return Date.class;
	}

	@Override
	public void validate(String fieldName, Date value) throws ValidationException {
		if (after != null) {
			if (afterInclusive && value.before(after)) {
				throw new ValidationException(fieldName, "Value must be on or after " + dateFormat.format(after));
			} else if (!afterInclusive && !value.after(after)) {
				throw new ValidationException(fieldName, "Value must be after " + dateFormat.format(after));
			}
		}
		if (before != null) {
			if (beforeInclusive && value.after(before)) {
				throw new ValidationException(fieldName, "Value must be on or before " + dateFormat.format(before));
			} else if (!beforeInclusive && !value.before(before)) {
				throw new ValidationException(fieldName, "Value must be before " + dateFormat.format(before));
			}
		}
	}
}
