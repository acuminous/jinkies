package modules

import geb.Module;


class Widget extends Module  {

	public void delete() {
		withConfirm {
			$('.delete').jquery.mouseover()
			$('.delete').jquery.click()
		}
	}
	
	public void update() {
		click()
	}
	
}
