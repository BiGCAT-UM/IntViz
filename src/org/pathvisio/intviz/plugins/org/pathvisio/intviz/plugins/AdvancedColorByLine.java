package org.pathvisio.intviz.plugins;
/*package org.pathvisio.intviz.plugins;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.bridgedb.Xref;
import org.jdom.Element;
import org.pathvisio.core.debug.Logger;
import org.pathvisio.core.view.ArrowShape;
import org.pathvisio.core.view.Graphics;
import org.pathvisio.core.view.Line;
import org.pathvisio.core.view.VPathway;
import org.pathvisio.data.DataException;
import org.pathvisio.data.IRow;
import org.pathvisio.data.ISample;
import org.pathvisio.desktop.gex.CachedData;
import org.pathvisio.desktop.gex.CachedData.Callback;
import org.pathvisio.desktop.gex.GexManager;
import org.pathvisio.desktop.gex.Sample;
import org.pathvisio.desktop.visualization.AbstractVisualizationMethod;
import org.pathvisio.desktop.visualization.ColorSet;
import org.pathvisio.desktop.visualization.ColorSetManager;
import org.pathvisio.desktop.visualization.VisualizationManager.VisualizationException;


 *//**
 * Visualization method for coloring by data:
 * can color a line by a value
 * @author anwesha
 */
/*
public class AdvancedColorByLine extends AbstractVisualizationMethod {

static final Color DEFAULT_POSCOLOR = Color.GREEN;
static final Color DEFAULT_NEGCOLOR = Color.RED;
static final Color DEFAULT_MINCOLOR = Color.BLUE;
static final Color DEFAULT_MEANCOLOR = Color.BLACK;
static final Color DEFAULT_MAXCOLOR = Color.YELLOW;
static final String DEFAULT_MINLINETHICKNESS = "1";
static final String DEFAULT_MAXLINETHICKNESS = "7";
static final String DEFAULT_MINDATAVALUE = "1";
static final String DEFAULT_MAXDATAVALUE = "10";

Color mincolor;
Color meancolor;
Color maxcolor;
Color poscolor;
Color negcolor;
Color c = Color.BLACK;
String minlinethickness;
String maxlinethickness;
String mindatavalue;
String meanatavalue;
String maxatavalue;
String rulexpression;
String maxdatavalue;
URL imageURL;



private List<ConfiguredSample> useSamples = new ArrayList<ConfiguredSample>();
private final GexManager gexManager;
private final ColorSetManager csm;
GexManager getGexManager() { return gexManager; }

 *//**
 * @param gexManager
 * @param csm
 */
/*
public AdvancedColorByLine(GexManager gexManager, ColorSetManager csm) {
this.gexManager = gexManager;
this.csm = csm;
setIsConfigurable(true);
setUseProvidedArea(true);
}

@Override
public Component visualizeOnToolTip(Graphics g) {
// TODO Auto-generated method stub
return null;
}

 *//**
 * Set a colorset
 */
/*
public void setSingleColorSet(ColorSet cs) {
for(ConfiguredSample s : useSamples) {
	s.setColorSet(cs);
}
}

 *//**
 * Get a colorset
 */
/*
public ColorSet getSingleColorSet() {
ColorSet cs = null;
for(ConfiguredSample s : useSamples) {
	if(cs == null) {
		cs = s.getColorSet();
	}
}
return cs;
}

 *//**
 * Get the configured sample for the given sample. Returns
 * null when no configured sample is found.
 */
/*
public ConfiguredSample getConfiguredSample(ISample iSample) {
for(ConfiguredSample cs : useSamples) {
	if(cs.getSample() != null && cs.getSample() == iSample)
		return cs;
}
return null;
}

@Override
public String getDescription() {
return "Change colour and thickness according to data";
}

@Override
public String getName() {
return "Data on lines ";
}

@Override
public JPanel getConfigurationPanel() {
return new ColorByLinePanel(this, csm);
}

public List<ConfiguredSample> getConfiguredSamples() {
return useSamples;
}

public List<ISample> getSelectedSamples() {
List<ISample> samples = new ArrayList<ISample>();

for(ConfiguredSample cs : useSamples)
{
	samples.add(cs.getSample());
}
return samples;
}

@Override
public void visualizeOnDrawing(Graphics g, Graphics2D g2d) {
if(g instanceof Line)
{
	if(useSamples.size() == 0) return; //Nothing to draw
	Line gp = (Line) g;
	drawArea(gp, g, g2d);
}
return;
}

void drawArea(final Line gp, Graphics g, Graphics2D g2d) {
int nr = useSamples.size();
g2d.setClip(null);

for(int i = 0; i < nr; i++)
{
	ConfiguredSample s = useSamples.get(i);
	Xref idc = new Xref(gp.getPathwayElement().getElementID(), gp.getPathwayElement().getDataSource());
	CachedData cache = gexManager.getCachedData();
	if(cache == null) {
		continue;
	}

	if(s.getColorSet() == null) {
		Logger.log.trace("No colorset for sample " + s);
		continue; //No ColorSet for this sample
	}
	if(cache.hasData(idc))
	{
		List<? extends IRow> data = cache.getData(idc);
		if (data.size() > 0)
		{
			drawSample(s, data, gp, g2d);
		}
	}
	else
	{
		cache.asyncGet(idc, new Callback()
		{
			@Override
			public void callback()
			{
				gp.markDirty();
			}
		});
	}
}
}

private void drawSample(ConfiguredSample s, List<? extends IRow> data,
	Line gp, Graphics2D g2d) {
ColorSet cs = s.getColorSet();
Color rgb = cs.getColor(data.get(0), s.getSample());
ISample sample = s.getSample();
IRow dataval = data.get(0);

drawColoredLine(gp, rgb, g2d, dataval, sample);
}

private void drawColoredLine(Line gp, Color rgb, Graphics2D g2d, IRow dataval, ISample sample){
g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
// Color c = cs.getColor(dataval, sample);
g2d.setColor(rgb);
g2d.setStroke(new BasicStroke(1));

g2d.draw(gp.getVConnectorAdjusted());

ArrowShape[] heads = gp.getVHeadsAdjusted();
ArrowShape hs = heads[0];
ArrowShape he = heads[1];

drawHead(g2d, he, 1, rgb);
drawHead(g2d, hs, 1, rgb);
}

// if(text) {
// g.setColor(Color.BLACK);
// int margin = 30; //Border spacing
// int x = bounds.x + margin / 2;
// int w = bounds.width - margin;
// for(int i = 0; i < colorValuePairs.size(); i++) {
// String value = "" + colorValuePairs.get(i).getValue();
// Rectangle2D fb = g.getFontMetrics().getStringBounds(value, g);
// g.drawString(
// value,
// x - (int)(fb.getWidth() / 2),
// bounds.y + bounds.height / 2 + (int)(fb.getHeight() / 2)
// );
// x += w / (colorValuePairs.size() - 1);
// }
// }


 * private void drawColoredLine(Line gp, Color rgb, Graphics2D g2d, IRow
 * dataval, ISample sample){ g2d.setPaint(rgb); g2d.setColor(rgb); c = rgb;
 * 
 * getMinColor(true); getMeanColor(true); getMaxColor(true);
 * 
 * // Color pc = getPosColor(true); // Color nc = getNegColor(true);
 * 
 * dataval.getSampleData(sample);
 * 
 * // if(datavalue >= 0){ // g2d.setPaint (pc); // g2d.setColor(pc); // c=
 * pc; // } // else{ // g2d.setPaint (nc); // g2d.setColor(nc); // c = nc;
 * // datavalue = datavalue * (-1); // }
 * 
 * // float lt= setLineThickness((float)datavalue);
 * 
 * int ls = gp.getPathwayElement().getLineStyle(); if (ls == 0) {
 * g2d.setStroke(new BasicStroke(1)); } else if (ls == 1) { g2d.setStroke
 * (new BasicStroke ( 1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10,
 * new float[] {4, 4}, 0)); }
 * 
 * g2d.draw(gp.getVConnectorAdjusted());
 * 
 * ArrowShape[] heads = gp.getVHeadsAdjusted(); ArrowShape hs = heads[0];
 * ArrowShape he = heads[1];
 * 
 * drawHead(g2d, he, 1, c); drawHead(g2d, hs, 1, c); }

protected void drawHead(Graphics2D g2d, ArrowShape arrow, float lt, Color rgb)
{
if(arrow != null)
{
	// reset stroked line to solid, but use given thickness
	g2d.setStroke(new BasicStroke(lt));

	switch (arrow.getFillType())
	{
	case OPEN:
		g2d.setPaint (Color.WHITE);
		g2d.setColor (rgb);
		g2d.draw (arrow.getShape());
		break;
	case CLOSED:
		g2d.setPaint (rgb);
		g2d.fill (arrow.getShape());
		g2d.draw (arrow.getShape());
		break;
	case WIRE:
		g2d.setColor (rgb);
		g2d.draw (arrow.getShape());
		break;
	default:
		assert (false);
	}
}
}

void setUseSamples(List<ConfiguredSample> samples)
{
useSamples = samples;
}

 *//**
 * Add a sample to use for visualization
 * @param s The sample to add
 */
/*
public void addUseSample(Sample s) {
if(s != null) {
	if(!useSamples.contains(s)) {
		ConfiguredSample consam = new ConfiguredSample(s);
		useSamples.add(consam);
	}
	modified();
}
}

 *//**
 * Remove a sample from the samples that will be used for visualization
 * @param s
 */
/*
void removeUseSample(ConfiguredSample s) {
if(s != null) {
	useSamples.remove(s);
	modified();
}
}

static final String XML_ATTR_MINCOLOR = "mincolor";
static final String XML_ATTR_MEANCOLOR = "meancolor";
static final String XML_ATTR_MAXCOLOR = "maxcolor";
static final String XML_ATTR_POSCOLOR = "poscolor";
static final String XML_ATTR_NEGCOLOR = "negcolor";
static final String XML_ATTR_MINLINETHICKNESS = "minlinethickness";
static final String XML_ATTR_MAXLINETHICKNESS = "maxlinethickness";
static final String XML_ELM_ID = "sample-id";

@Override
public final Element toXML() {
Element xml = super.toXML();
xml.setAttribute(XML_ATTR_MINCOLOR, getMinColor(true).toString());
xml.setAttribute(XML_ATTR_MEANCOLOR, getMeanColor(true).toString());
xml.setAttribute(XML_ATTR_MAXCOLOR, getMaxColor(true).toString());
xml.setAttribute(XML_ATTR_POSCOLOR, getPosColor(true).toString());
xml.setAttribute(XML_ATTR_NEGCOLOR, getNegColor(true).toString());
xml.setAttribute(XML_ATTR_MINLINETHICKNESS, getMinThickness(true));
xml.setAttribute(XML_ATTR_MAXLINETHICKNESS, getMaxThickness(true));
for(ConfiguredSample s : useSamples)
{
	if (s.getColorSet() != null) {
		xml.addContent(s.toXML());
	}
	Element selm = new Element(XML_ELM_ID);
	selm.setText(Integer.toString(s.getId()));
	xml.addContent(selm);
}
return xml;
}

@Override
public final void loadXML(Element xml) throws VisualizationException {
super.loadXML(xml);
for(Object o : xml.getChildren(ConfiguredSample.XML_ELEMENT)) {
	try {
		useSamples.add(new ConfiguredSample((Element) o));
	} catch(VisualizationException e) {
		Logger.log.error("Unable to load plugin settings", e);
	}
	setMinColor(Color.decode(xml.getAttributeValue(XML_ATTR_MINCOLOR)));
	setMeanColor(Color
			.decode(xml.getAttributeValue(XML_ATTR_MEANCOLOR)));
	setMaxColor(Color.decode(xml.getAttributeValue(XML_ATTR_MAXCOLOR)));
	setPosColor(Color.decode(xml.getAttributeValue(XML_ATTR_POSCOLOR)));
	setNegColor(Color.decode(xml.getAttributeValue(XML_ATTR_NEGCOLOR)));
	setMinThickness(xml.getAttributeValue(XML_ATTR_MINLINETHICKNESS));
	setMaxThickness(xml.getAttributeValue(XML_ATTR_MAXLINETHICKNESS));
}
}

 *//**
 * Set color to visualize positive and negative values of data on lines
 * Set choose a minimum and maximum linethickness to create a gradient
 * of linethicknesses to visualize on lines
 */
/*
protected void setMinColor(Color pc) {
if (pc != null) {
	mincolor = pc;
	modified();
}
}

private Color getMinColor(boolean a) {
Color pc = mincolor == null ? DEFAULT_MINCOLOR : mincolor;
VPathway vp = getVisualization().getManager().getEngine().getActiveVPathway();
if(vp != null) {
	pc = new Color(pc.getRGB());
}
return pc;
}


protected void setMeanColor(Color pc) {
if (pc != null) {
	meancolor = pc;
	modified();
}
}

private Color getMeanColor(boolean a) {
Color pc = meancolor == null ? DEFAULT_MEANCOLOR : mincolor;
VPathway vp = getVisualization().getManager().getEngine().getActiveVPathway();
if(vp != null) {
	pc = new Color(pc.getRGB());
}
return pc;
}

protected void setMaxColor(Color pc) {
if (pc != null) {
	maxcolor = pc;
	modified();
}
}

private Color getMaxColor(boolean a) {
Color pc = maxcolor == null ? DEFAULT_MAXCOLOR : mincolor;
VPathway vp = getVisualization().getManager().getEngine().getActiveVPathway();
if(vp != null) {
	pc = new Color(pc.getRGB());
}
return pc;
}

protected void setPosColor(Color pc){
if (pc !=null ){
	poscolor = pc;
	modified();
}
}

private Color getPosColor(boolean a) {
Color pc = poscolor == null ? DEFAULT_POSCOLOR : poscolor;
VPathway vp = getVisualization().getManager().getEngine().getActiveVPathway();
if(vp != null) {
	pc = new Color(pc.getRGB());
}
return pc;
}

protected void setNegColor(Color nc){
if (nc != null){
	negcolor = nc;
	modified();
}
}

private Color getNegColor(boolean b) {
Color nc = negcolor == null ? DEFAULT_NEGCOLOR : negcolor;
VPathway vp = getVisualization().getManager().getEngine().getActiveVPathway();
if(vp != null) {
	nc = new Color(nc.getRGB());
}
return nc;
}

protected void setMinThickness(String minT) {
if (minT !=null ){
	minlinethickness = minT;
	modified();
}

}

private String getMinThickness(boolean a) {
String minT = minlinethickness == null ? DEFAULT_MINLINETHICKNESS : minlinethickness;
VPathway vp = getVisualization().getManager().getEngine().getActiveVPathway();
if(vp != null) {
	minT = new String(minT);
}
return minT;
}

protected void setMaxThickness(String maxT) {
if (maxT !=null ){
	maxlinethickness = maxT;
	modified();
}

}

private String getMaxThickness(boolean a) {
String maxT = maxlinethickness == null ? DEFAULT_MAXLINETHICKNESS : maxlinethickness;
VPathway vp = getVisualization().getManager().getEngine().getActiveVPathway();
if(vp != null) {
	maxT = new String(maxT);
}
return maxT;
}

protected void setMinData(String minD) {
if (minD !=null ){
	mindatavalue = minD;
	modified();
}
}

protected void setMaxData(String maxD) {
if (maxD !=null ){
	maxdatavalue = maxD;
	modified();
}
}

protected void setRulExp(String expR) {
if (expR != null) {
	rulexpression = expR;
	modified();
}
}

 *//**
 * This class stores the configuration for a sample that is selected for
 * visualization. In this implementation, a color-set to use for
 * visualization is stored. Extend this class to store additional
 * configuration data.
 */
/*

public class ConfiguredSample {

ColorSet colorSet = new ColorSet(csm);

int tolerance; // range 0 - 255;

private ISample sample;

public ISample getSample() {
	return sample;
}

public int getId() {
	return sample.getId();
}

static final String XML_ELEMENT = "sample";
static final String XML_ATTR_ID = "id";
static final String XML_ATTR_COLORSET = "colorset";

private final Element toXML() {
	Element xml = new Element(XML_ELEMENT);
	xml.setAttribute(XML_ATTR_ID, Integer.toString(sample.getId()));
	xml.setAttribute(XML_ATTR_COLORSET, colorSet.getName());
	return xml;
}

private final void loadXML(Element xml) throws VisualizationException {
	int id = Integer.parseInt(xml.getAttributeValue(XML_ATTR_ID));

	String csn = xml.getAttributeValue(XML_ATTR_COLORSET); try { sample =
			gexManager.getCurrentGex().getSamples().get(id); } catch (DataException
					e) { // TODO Auto-generated catch block e.printStackTrace(); }

				if (sample == null) throw new
				VisualizationException("Couldn't find Sample with id " + id);

				setColorSet(getVisualization().getManager().getColorSetManager().getColorSet
						(csn));
			}
}

 *//**
 * Create a configured sample based on an existing sample
 * 
 * @param s
 *            The sample to base the configured sample on
 */
/*

public ConfiguredSample(ISample s) {
if (s == null)
	throw new NullPointerException();
sample = s;
}

protected AdvancedColorByLine getMethod() {
return AdvancedColorByLine.this;
}

 *//**
 * Create a configured sample from the information in the given XML
 * element
 * 
 * @param xml
 *            The XML element containing information to create the
 *            configured sample from
 * @throws VisualizationException
 */
/*

public ConfiguredSample(Element xml) throws VisualizationException {
loadXML(xml);
}

 *//**
 * Set the color-set to use for visualization of this sample
 */
/*

public void setColorSet(ColorSet cs) {
colorSet = cs;
modified();
}

 *//**
 * Get the color-set to use for visualization of this sample
 * 
 * @return the color-set
 */
/*

protected ColorSet getColorSet() {
return colorSet;
}

 *//**
 * Get the name of the color-sets that is selected for visualization
 * 
 * @return The name of the selected color-set, or
 *         "no colorsets available", if no color-sets exist
 */
/*

protected String getColorSetName() {
ColorSet cs = getColorSet();
return cs == null ? "no colorsets available" : cs.getName();
}


@Override
public int defaultDrawingOrder()
{
return -1;
}

 *//**
 * Check whether advanced settings are used
 */
/*
 * public boolean isAdvanced() { // Advanced when different colorsets or an
 * image is specified if (useSamples.size() == 0) return false; for
 * (ConfiguredSample cs : useSamples) { if (cs.getURL() != null) return true; }
 * return getSingleColorSet() == null; }
 * 
 * public void setURL(URL url) { imageURL = url; invalidateImageCache();
 * modified(); }
 * 
 * public void setDefaultURL() { setURL(defaultURLs().get(0)); }
 * 
 * public URL getURL() { return imageURL; }
 * 
 * public boolean hasImage() { return imageURL != null; } } }
 */