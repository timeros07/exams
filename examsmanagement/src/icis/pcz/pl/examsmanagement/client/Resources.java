package icis.pcz.pl.examsmanagement.client;

import com.google.gwt.resources.client.ClientBundle;

import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle 
{
	@Source("images/person-icon.png")
	ImageResource person_image();
	
	@Source("images/exam-icon.png")
	ImageResource exam_image();
	
	@Source("images/result-icon.png")
	ImageResource result_image();
	
	@Source("images/correct_icon.jpg")
	ImageResource correct_image();
	
	@Source("images/incorrect_icon.jpg")
	ImageResource incorrect_image();
	
	@Source("images/alert.png")
	ImageResource alert_image();
}
