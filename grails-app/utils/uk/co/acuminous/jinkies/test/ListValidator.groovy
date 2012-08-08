package uk.co.acuminous.jinkies.test

class ListValidator {
	
	static containsDuplicates = { List list, def obj ->
		if (list?.size() != list?.unique()?.size()) {
			['duplicate']
		}
		
	}
	
}
