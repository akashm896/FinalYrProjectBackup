package wilos.presentation.web.balise;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentBodyTag;
import javax.faces.webapp.UIComponentTag;

public class InfoBulleTag extends UIComponentTag {

	private String marginleft;
	private String marginright;
	private String margintop;
	private String maxwidth;

	public String getMaxwidth() {
		return maxwidth;
	}

	public void setMaxwidth(String maxwidth) {
		this.maxwidth = maxwidth;
	}

	public String getMargintop() {
		return margintop;
	}

	public void setMargintop(String margintop) {
		this.margintop = margintop;
	}

	private String balloontype;
	
	public String getBalloontype() {
		return balloontype;
	}

	public void setBalloontype(String balloontype) {
		this.balloontype = balloontype;
	}

	public String getMarginright() {
		return marginright;
	}

	public void setMarginright(String marginright) {
		this.marginright = marginright;
	}


	public String getMarginleft() {
		return marginleft;
	}

	public void setMarginleft(String marginleft) {
		this.marginleft = marginleft;
	}

	public String getRendererType() {
		return null;
	}

	public String getComponentType() {
		return "facestut.component.InfoBulle";
	}

	public boolean getRendersChildren() {
		return true;
	}

	public void setProperties(UIComponent component) {
		super.setProperties(component);
		if (getMarginleft() != null) {
			component.getAttributes().put("marginleft", getMarginleft());
		}
		if (getMarginright() != null) {
			component.getAttributes().put("marginright", getMarginright());
		}
		if (getBalloontype() != null) {
			component.getAttributes().put("balloontype", getBalloontype());
		}
		if (getMargintop() != null) {
			component.getAttributes().put("margintop", getMargintop());
		}
		if (getMaxwidth() != null) {
			component.getAttributes().put("maxwidth", getMaxwidth());
		}
	}
}
