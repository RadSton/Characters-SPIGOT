package radston12.skin;

public class Skin {

	private String texture;
	private String signature;
	
	public Skin(String t, String s) {
		this.texture = t;
		this.signature = s;
	}
	
	public Skin() {} // DO NOTHING
	
	public String getTexture() {
		return texture;
	}
	
	public String getSignature() {
		return signature;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public void setTexture(String texture) {
		this.texture = texture;
	}
}
