# Zenput

DISCLAIMER: Zenput is a beta product at this time; some of it's functionality is being updated at this time and tests need to be written for it.

Zenput is an input validation framework for Java Swing.  The primary construct of the Zenput framework is the Processor interface, and the InputProcessor and SourceProcessor classes that implement that interface.  The general validation flow is:

Source Object --> Conversion --> Input Component

Input Component --> Conversion --> Validation --> Source Object

Validation errors can happen at both the conversion and validation steps.  Conversion converts the data type between what the user interface component uses and how the data is stored, while validation verifies the data as stored conforms to expected rules.  For example, an input component may use a String to input data that is stored as an Integer; in such case, a validation error can be thrown by the converter if the String cannot be converted to an Integer, but a validator may further ensure that the Integer value is within appropriate minimum and maximum expected values.

To initialize the system, fields from the source object and any validators for those fields must be registered with the processor.  This can be done with the SourceProcessor class.  However, much of this work can be handled through an annotation-based approach by adding appropriate annotations to the Source Object class' fields.

The processor can then be wrapped by an InputProcessor, which provides the means to link those fields to user interface input components.

Once a processor is constructed, validate methods can be called to validate some or all fields.  After validate is called, getError methods can be called to retrieve any validation errors.  The InputProcessor provides additional options on how validation operates and when data from the input compoonents gets committed back to the source object.  You can also set how to change the appearance of invalid user inputs with the setDefaultMarkerBuilder method.  Zenput provides a small collection of markers that can be used to style common Swing input components.

Putting it all into an example, assume you have a Source Class as follows:

	public class CookieOrder {
		private Integer quantity;
		public Integer getQuantity() {
			return quantity;
		}
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
	}	

For any cookie order, the quantity should be at least 1 but no more than 100.  You could construct a source processor as follows:

	CookieOrder cookieOrder = new CookieOrder();
	cookieOrder.setQuantity(getSomeValue());
	SourceProcessor processor = new SourceProcessor(cookieOrder);
	processor.registerField("quantity", "Quantity Ordered");
	processor.registerValidator("quantity", new RequiredValidator());
	processor.registerValidator("quantity", new IntegerValidator(1, 100));
	processor.validate();
	System.out.println("There are " + processor.getErrors().size() + " validation errors.");

However, a cleaner approach would be to use annotations.  You could revise the CookieOrder class as follows:

	public class CookieOrder {
	
		@InputField(title="Quantity Ordered")
		@ValidateRequired
		@ValidateInteger(min=1,max=100)
		private Integer quantity;
	
		public Integer getQuantity() {
			return quantity;
		}
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
	}
	
Then create the processor as follows:

	CookieOrder cookieOrder = new CookieOrder();
	cookieOrder.setQuantity(getSomeValue());
	SourceProcessor processor = new SourceProcessor(cookieOrder);
	processor.validate();
	System.out.println("There are " + processor.getErrors().size() + " validation errors.");
	
Of course, you probably have an input field for the user to enter the value with.  You then need to create an InputProcessor to handle it:

	CookieOrder cookieOrder = new CookieOrder();
	JTextField quantityInputField = new JTextField();
	SourceProcessor sourceProcessor = new SourceProcessor(cookieOrder);
	InputProcessor inputProcessor = new InputProcessor(sourceProcessor, CommitMode.COMMIT_ALL, false);
	inputProcessor.registerInput("quantity", quantityInputField);
	... user enters value on text field and saves ...
	processor.validate();
	System.out.println("There are " + processor.getErrors().size() + " validation errors.");