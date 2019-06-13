package translator;

public class DataParsed {
	
	private String box1;
	private String box2;
	
	public DataParsed(String a,String b) {
		this.box1 = a;
		this.box2 = b;
	}
	
	public String getFirstBox() {
		return this.box1;
	}
	public String getSecondBox() {
		return this.box2;
	}
	public void setFirstBox(String s) {
		 this.box1 = s;
	}
	public void setSecondBox(String s) {
		this.box2 = s;
	}

	public Boolean equals(DataParsed a,DataParsed b) {
		return a.getFirstBox()==b.getFirstBox() && a.getFirstBox() == b.getSecondBox();
	}
	public String getSecondWithFirstBox(String s){
		 if ( this.box1 == s ) return this.box2;
		return null;
	}
	public String getFirstWithSecondBox(String s){
		 if ( this.box2 == s ) return this.box2;
		return null;
	}
}
