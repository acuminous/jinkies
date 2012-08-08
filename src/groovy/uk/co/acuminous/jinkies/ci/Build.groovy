package uk.co.acuminous.jinkies.ci


class Build {
	
	String url
	Integer number	
	String result
	Long timestamp
	Job job	

	boolean isSuccess() {
		result == 'success'
	}
			
	@Override
	boolean equals(Object other) {
		this.is(other) || (
			other != null &&
			other.class == this.class &&
			other.url == this.url
		)
	}
	
	@Override
	int hashCode() {
		url.hashCode()
	}
	
	@Override
	String toString() {
		"Build[url=$url,result=$result,timestamp=$timestamp]"
	}
}
