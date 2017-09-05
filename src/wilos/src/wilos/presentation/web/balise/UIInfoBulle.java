package wilos.presentation.web.balise;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class UIInfoBulle extends UIComponentBase {

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
	
	public String getFamily() {
		return "facestut";
	}

	public void encodeBegin(FacesContext facesContext) throws IOException {
		ResponseWriter responseWriter = facesContext.getResponseWriter();
		String style = "position:absolute;z-index:400;";
		//margin
		if(marginleft != ""){
			style = style + "margin-left:"+marginleft+";";
		}
		if(marginright != ""){
			style = style + "margin-right:"+marginright+";";
		}
		if(margintop != ""){
			style = style + "margin-top:"+margintop+";";
		}
		
		responseWriter.startElement("div", this);
		responseWriter.writeAttribute("style",style, "style");
		responseWriter.writeAttribute("class", "infos", "class");
		
		//type de bulle
		if("fhd".equals(balloontype)){
			responseWriter.startElement("div", this);
			responseWriter.writeAttribute("style","text-align:right;","style");
			responseWriter.startElement("img", this);
			responseWriter.writeAttribute("src", "images/infobulleflechehautdroite.gif", "src");
			responseWriter.endElement("img");
			responseWriter.endElement("div");
		}else if("fhg".equals(balloontype)){
			responseWriter.startElement("div", this);
			responseWriter.writeAttribute("style","text-align:left;","style");
			responseWriter.startElement("img", this);
			responseWriter.writeAttribute("src", "images/infobulleflechehautgauche.gif", "src");
			responseWriter.endElement("img");
			responseWriter.endElement("div");
		}	
		
		responseWriter.startElement("div", this);
		if(maxwidth != ""){
			responseWriter.writeAttribute("style","width:"+maxwidth+";","style");
		}
		responseWriter.writeAttribute("class", "divinfos", "class");
	}

	public void encodeChildren(FacesContext facesContext) throws IOException {
		for (Iterator iter1 = getChildren().iterator(); iter1.hasNext();) {
			UIComponent component = (UIComponent) iter1.next();
			component.encodeBegin(facesContext);
			component.encodeChildren(facesContext);
			component.encodeEnd(facesContext);
		}
	}
	
	public void encodeEnd(FacesContext facesContext) throws IOException {
		ResponseWriter responseWriter = facesContext.getResponseWriter();
		responseWriter.endElement("div");
		if("fbd".equals(balloontype)){
			responseWriter.startElement("div", this);
			responseWriter.writeAttribute("style","text-align:right;","style");
			responseWriter.startElement("img", this);
			responseWriter.writeAttribute("src", "images/infobulleflechebasdroite.gif", "src");
			responseWriter.endElement("img");
			responseWriter.endElement("div");
		}else if("fbg".equals(balloontype)){
			responseWriter.startElement("div", this);
			responseWriter.writeAttribute("style","text-align:left;","style");			
			responseWriter.startElement("img", this);
			responseWriter.writeAttribute("src", "images/infobulleflechebasgauche.gif", "src");
			responseWriter.endElement("img");
			responseWriter.endElement("div");
		}
		responseWriter.endElement("div");
	}

	public void decode(FacesContext facesContext) {
	}

	public boolean getRendersChildren() {
		return true;
	}

}
