# Zenput

Zenput is an input validation framework for Java Swing.  The primary construct of the Zenput framework is the Processor interface, and the InputProcessor and SourceProcessor classes that implement that interface.  The general validation flow is:

Source Object --> Conversion --> Input Component

Input Component --> Conversion --> Validation --> Source Object

Validation errors can happen at both the conversion and validation steps.  Conversion converts the data type between what the user interface component uses and how the data is stored, while validation verifies the data as stored conforms to expected rules.  For example, an input component may use a String to input data that is stored as an Integer; in such case, a validation error can be thrown by the converter if the String cannot be converted to an Integer, but a validator may further ensure that the Integer value is within appropriate minimum and maximum expected values.

To initialize the system, fields from the source object and any validators for those fields must be registered with the processor.  This can be done with the SourceProcessor class.  However, much of this work can be handled through an annotation-based approach by adding appropriate annotations to the Source Object class' fields.

The processor can then be wrapped by an InputProcessor, which provides the means to link those fields to user interface input components.

Once a processor is constructed, validate methods can be called to validate some or all fields.  After validate is called, getError methods can be called to retrieve any validation errors.  The InputProcessor provides additional options on how validation operates and when data from the input compoonents gets committed back to the source object.  You can also set how to change the appearance of invalid user inputs with the setDefaultMarkerBuilder method.  Zenput provides a small collection of markers that can be used to style common Swing input components.

## Processors

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

## Markers

Typically, you would also want to mark the input field in some way to the user to indicate when the field value is invalid.  To do this, either set a default marker builder for the input field type or, after registering the input, set a specific marker for the input field.

	inputProcessor.setDefaultMarkerBuilder(JTextField.class, MarkerFactory.backgroundMarkerBuilder());

or

	inputProcessor.registerInput("quantity", quantityInputField);
	inputProcessor.setMarker("quantity", new BackgroundMarker());
	
The first approach will set a common marker for all input fields of the specified type, whereas the second approach will set the marker just for the specified field.  Setting a specific marker for a field will override any default marker that would otherwise apply.

## Validation Controls

Input fields can also have their validation dependent on various conditions. A control determines whether or not the rest of the validations on the field will be executed or not.  For example, you may have an input field that requires a value, but only if another input field checkbox is checked:

	public class CookieOrder {
	
		@InputField(title="Order Cookies?")
		private boolean orderCookies;
		
		@InputField(title="Quantity Ordered")
		@ControlEquals(dependencyOn="orderCookies", valueType=Boolean.class, stringValue="true")
		@ValidateRequired
		@ValidateInteger(min=1,max=100)
		private Integer quantity;
		
		public boolean isOrderCookies() {
			return orderCookies;
		}
		public void setOrderCookies(boolean orderCookies) {
			this.orderCookies = orderCookies;
		}
		public Integer getQuantity() {
			return quantity;
		}
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
	}	 

## Validation Dependencies

In addition to being able to enable or disable validation on a field with control dependencies, some fields may need validations that are dependent on the values of other fields.  Assume our cookie distributor can gift wrap some or all of the cookies, but they still want the order system to ask for the total quantity of cookies, just with an extra input that asks how many of the cookies should be gift wrapped.  In such case, the quantityWrapped field would need to be less than or equal to the quantity field.

	public class CookieOrder {
	
		@InputField(title="Order Cookies?")
		private boolean orderCookies;
		
		@InputField(title="Quantity Ordered")
		@ControlEquals(dependencyOn="orderCookies", valueType=Boolean.class, stringValue="true")
		@ValidateRequired
		@ValidateInteger(min=1,max=100)
		private Integer quantity;
		
		@InputField(title="Quantity Wrapped")
		@ControlEquals(dependencyOn="orderCookies", valueType=Boolean.class, stringValue="true")
		@ValidateRequired
		@ValidateDependencyNumeric(dependencyOn="quantity", operator=Operator.LTEQ)
		@ValidateInteger(min=0)		
		private Integer quantityWrapped;
		
		public boolean isOrderCookies() {
			return orderCookies;
		}
		public void setOrderCookies(boolean orderCookies) {
			this.orderCookies = orderCookies;
		}
		public Integer getQuantity() {
			return quantity;
		}
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
	}

## Validation Messages

Validation messages are tied to message keys which are looked up from a message properties file.  Zenput has a defualt message properties file, but a customized version can be used when building messages for display to the user.  You can set the message properties on the Processor, after which it will be used when building a validation message dialog with the ValidationErrorUtil and may also be used by Markers:

	Properties properties = getMyProperties();
	processor.setMessageProperties(properties);
	...
	ValidationErrorUtil.showMessageDialog(...)


