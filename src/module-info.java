module Nagin {
	requires javafx.controls;
	
	opens application to javafx.graphics, javafx.fxml;
//	exports application to javafx.graphics, javafx.fxml;
}
