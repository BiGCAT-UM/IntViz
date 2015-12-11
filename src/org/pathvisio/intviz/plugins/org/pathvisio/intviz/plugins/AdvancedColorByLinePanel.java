package org.pathvisio.intviz.plugins;
/*// PathVisio,
// a tool for data visualization and analysis using Biological Pathways
// Copyright 2006-2011 BiGCaT Bioinformatics
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package org.pathvisio.intviz.plugins;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.bridgedb.gui.SimpleFileFilter;
import org.pathvisio.core.debug.Logger;
import org.pathvisio.core.util.Resources;
import org.pathvisio.data.ISample;
import org.pathvisio.desktop.visualization.ColorSet;
import org.pathvisio.desktop.visualization.ColorSetManager;
import org.pathvisio.gui.dialogs.OkCancelDialog;
import org.pathvisio.intviz.plugins.ColorByLine.ConfiguredSample;
import org.pathvisio.visualization.gui.ColorSetChooser;
import org.pathvisio.visualization.gui.ColorSetCombo;
import org.pathvisio.visualization.plugins.SortSampleCheckList;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

 *//**
 * Configuration panel for the ColorByLine visualization method
 * 
 * @author anwesha
 */
/*
public class AdvancedColorByLinePanel extends JPanel implements ActionListener {
private static final long serialVersionUID = 1L;

static final String ACTION_BASIC = "Basic";
static final String ACTION_ADVANCED = "Advanced";
static final String ACTION_SAMPLE = "sample";
static final String ACTION_OPTIONS = "options";

static final ImageIcon COLOR_PICK_ICON = new ImageIcon(
		Resources.getResourceURL("colorpicker.gif"));
static final Cursor COLOR_PICK_CURS = Toolkit.getDefaultToolkit()
		.createCustomCursor(COLOR_PICK_ICON.getImage(), new Point(4, 19),
				"Color picker");

private final ColorByLine method;
private final Basic basic;
private final Advanced advanced;
private final CardLayout cardLayout;
private final JPanel settings;
private final ColorSetManager csm;
JButton options = new JButton("Set Visualization");

 *//**
 * @param method
 * @param csm
 */
/*
public AdvancedColorByLinePanel(ColorByLine method, ColorSetManager csm) {
this.method = method;
this.csm = csm;

setLayout(new FormLayout(
		"4dlu, pref, 4dlu, pref, fill:pref:grow, 4dlu",
		"4dlu, pref, 4dlu, fill:pref:grow, 4dlu"));

ButtonGroup buttons = new ButtonGroup();
JRadioButton rbBasic = new JRadioButton(ACTION_BASIC);
rbBasic.setActionCommand(ACTION_BASIC);
rbBasic.addActionListener(this);
buttons.add(rbBasic);
JRadioButton rbAdvanced = new JRadioButton(ACTION_ADVANCED);
rbAdvanced.setActionCommand(ACTION_ADVANCED);
rbAdvanced.addActionListener(this);
buttons.add(rbAdvanced);

CellConstraints cc = new CellConstraints();
add(rbBasic, cc.xy(2, 2));
add(rbAdvanced, cc.xy(4, 2));

settings = new JPanel();
settings.setBorder(BorderFactory.createEtchedBorder());
cardLayout = new CardLayout();
settings.setLayout(cardLayout);

basic = new Basic();

advanced = new Advanced();
settings.add(basic, ACTION_BASIC);
settings.add(advanced, ACTION_ADVANCED);

add(settings, cc.xyw(2, 4, 4));

if (method.isAdvanced()) {
	rbAdvanced.doClick();
} else {
	rbBasic.doClick();
}
}

 *//** Panel for colorByExpression configuration in "advanced" mode */
/*
public class Advanced extends JPanel implements ActionListener,
ListDataListener, ListSelectionListener {
private static final long serialVersionUID = 1L;
SortSampleCheckList sampleList;
ColorSetCombo colorSetCombo;
SamplePanel samplePanel;

public Advanced() {
setLayout(new FormLayout(
"4dlu, fill:pref:grow(0.5), 4dlu, fill:pref:grow(0.5), 4dlu",
"4dlu, fill:pref:grow, 4dlu"));

sampleList = new SortSampleCheckList(method.getSelectedSamples(),
method.getGexManager());
sampleList.getList().addActionListener(this);
sampleList.getList().setActionCommand(ACTION_SAMPLE);
sampleList.getList().getModel().addListDataListener(this);
sampleList.getList().addListSelectionListener(this);
samplePanel = new SamplePanel(null);

refresh();
CellConstraints cc = new CellConstraints();
add(sampleList, cc.xy(2, 2));
add(samplePanel, cc.xy(4, 2));
}

void refresh() {
sampleList.getList()
.setSelectedSamples(method.getSelectedSamples());
samplePanel.setInput(method.getConfiguredSample(sampleList
.getList().getSelectedSample()));
}

@Override
public void actionPerformed(ActionEvent e) {
String action = e.getActionCommand();
if (ACTION_SAMPLE.equals(action)) {
refreshSamples();
}
}

private void refreshSamples() {
List<ConfiguredSample> csamples = new ArrayList<ConfiguredSample>();
for (ISample s : sampleList.getList().getSelectedSamplesInOrder()) {
ConfiguredSample cs = method.getConfiguredSample(s);
if (cs == null) {
cs = method.new ConfiguredSample(s);
}
csamples.add(cs);
if (sampleList.getList().getSelectedSample() == s) {
samplePanel.setInput(cs);
}
}
method.setUseSamples(csamples);
}

@Override
public void contentsChanged(ListDataEvent e) {
refreshSamples();
}

@Override
public void intervalAdded(ListDataEvent e) {
refreshSamples();
}

@Override
public void intervalRemoved(ListDataEvent e) {
refreshSamples();
}

@Override
public void valueChanged(ListSelectionEvent e) {
samplePanel.setInput(method.getConfiguredSample(sampleList
.getList().getSelectedSample()));
}
}

 *//** subPanel of the Advanced panel */
/*
class SamplePanel extends JPanel implements ActionListener {
static final String ACTION_IMG = "Use image";

ConfiguredSample cs;
ColorSetCombo colorSetCombo;
JCheckBox imageCheck;
JPanel imagePanel;
JComboBox imageCombo;
JLabel previewLabel;

public SamplePanel(ConfiguredSample cs) {
setInput(cs);
}

void setInput(ConfiguredSample cs) {
this.cs = cs;
removeAll();
if (cs == null) {
setBorder(BorderFactory.createTitledBorder(
BorderFactory.createEtchedBorder(), "Sample settings"));
setLayout(new BorderLayout());
add(new JLabel("Select a sample to configure"),
BorderLayout.CENTER);
} else {
setContents();
setBorder(BorderFactory.createTitledBorder(
BorderFactory.createEtchedBorder(),
"Sample settings for " + cs.getSample().getName()));
}
revalidate();
}

void setContents() {
DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(
"pref, 4dlu, fill:pref:grow"), this);

ColorSetManager csm = method.getVisualization().getManager()
.getColorSetManager();
ColorSetChooser csChooser = new ColorSetChooser(csm,
method.getGexManager());
colorSetCombo = csChooser.getColorSetCombo();
colorSetCombo.setActionCommand(ACTION_COMBO);
colorSetCombo.addActionListener(this);
colorSetCombo.getModel().setSelectedItem(cs.getColorSet());

imageCheck = new JCheckBox(ACTION_IMG);
imageCheck.setActionCommand(ACTION_IMG);
imageCheck.addActionListener(this);
imageCheck.setSelected(cs.getURL() != null);

imagePanel = new JPanel();
imagePanel.setBorder(BorderFactory.createTitledBorder(
BorderFactory.createEtchedBorder(), "Image settings"));

builder.setDefaultDialogBorder();
builder.append("Color set:", csChooser);
builder.nextLine();
builder.append(imageCheck, 3);
builder.nextLine();
builder.append(imagePanel, 3);

refreshImagePanel();
}

void refreshImagePanel() {
imagePanel.setEnabled(imageCheck.isSelected());
imagePanel.removeAll();

if (imagePanel.isEnabled()) {
imageCombo = new JComboBox(cs.getMethod().getImageURLs()
.toArray());
imageCombo.setSelectedItem(cs.getURL());
imageCombo.setRenderer(new DefaultListCellRenderer() {
@Override
public Component getListCellRendererComponent(JList list,
Object value, int index, boolean isSelected,
boolean cellHasFocus) {
JLabel lbl = (JLabel) super
.getListCellRendererComponent(list, value,
index, isSelected, cellHasFocus);
if (value instanceof URL
&& ((URL) value).getFile() != null) {
lbl.setText(new File(((URL) value).getFile())
.getName());
}
return lbl;
}
});
imageCombo.addActionListener(new ActionListener() {
@Override
public void actionPerformed(ActionEvent e) {
cs.setURL((URL) imageCombo.getSelectedItem());
refreshPreview();
}
});

JButton addImg = new JButton("Load image");
addImg.addActionListener(new ActionListener() {
@Override
public void actionPerformed(ActionEvent e) {
chooseImage();
}
});

previewLabel = new JLabel();
previewLabel.setOpaque(true);
previewLabel.setPreferredSize(new Dimension(75, 75));

final JPanel colorPanel = new JPanel();
colorPanel.setOpaque(true);
colorPanel.setBackground(cs.getReplaceColor());
colorPanel.setPreferredSize(new Dimension(15, 15));

final JButton pick = new JButton("Change");
pick.addActionListener(new ActionListener() {
@Override
public void actionPerformed(ActionEvent e) {
chooseColor();
colorPanel.setBackground(cs.getReplaceColor());
}
});
// Work on color picker
// pick.setIcon(COLOR_PICK_ICON);
// try {
// final Robot robot = new Robot();
// pick.addMouseListener(
// new MouseAdapter() {
// public void mousePressed(MouseEvent me) {
// SwingUtilities.getWindowAncestor(pick).setCursor(COLOR_PICK_CURS);
// }
// public void mouseReleased(MouseEvent me) {
// Point p = me.getPoint();
// Logger.log.trace("" + p);
// SwingUtilities.convertPointToScreen(p, me.getComponent());
// Logger.log.trace("" + p);
// Logger.log.trace("--");
// colorPanel.setBackground(robot.getPixelColor(p.x, p.y));
// SwingUtilities.getWindowAncestor(pick).setCursor(Cursor.getDefaultCursor());
// }
// });
//
// pick.addMouseMotionListener(
// new MouseMotionAdapter() {
// public void mouseDragged(MouseEvent me) {
// Point p = me.getPoint();
// Logger.log.trace("" + p);
// SwingUtilities.convertPointToScreen(p, me.getComponent());
// Logger.log.trace("" + p);
// Logger.log.trace("--");
// Color c = robot.getPixelColor(p.x, p.y);
// colorPanel.setBackground(c);
// cs.setReplaceColor(c);
// }
// });
// } catch(AWTException e) {
// Logger.log.error("Unable to create robot for color picker");
// }

final JSpinner tolerance = new JSpinner(new SpinnerNumberModel(
cs.getTolerance(), 0, 255, 1));
tolerance.addChangeListener(new ChangeListener() {
@Override
public void stateChanged(ChangeEvent e) {
cs.setTolerance((Integer) tolerance.getValue());
refreshPreview();
}
});

imagePanel
.setLayout(new FormLayout(
"4dlu, fill:pref:grow, 4dlu, pref, 4dlu, fill:10dlu, 4dlu, pref, 4dlu",
"4dlu, pref, 4dlu, pref:grow, 4dlu, pref, 4dlu"));
CellConstraints cc = new CellConstraints();
imagePanel.add(imageCombo, cc.xy(2, 2));
imagePanel.add(addImg, cc.xy(4, 2));
imagePanel.add(previewLabel, cc.xywh(2, 4, 1, 2, "fill, fill"));
imagePanel.add(new JLabel("Transparent color:"), cc.xy(4, 4));
imagePanel.add(colorPanel, cc.xy(6, 4));
imagePanel.add(pick, cc.xy(8, 4));
imagePanel.add(new JLabel("Tolerance:"), cc.xy(4, 6));
imagePanel.add(tolerance, cc.xyw(6, 6, 3));

imagePanel.setVisible(true);
refreshPreview();
} else {
imagePanel.setVisible(false);
}
}

void refreshPreview() {
Dimension size = previewLabel.getSize();
if (size.width <= 0 && size.height <= 0) {
size = previewLabel.getPreferredSize();
}
Image img = cs.getImage(size, Color.GRAY);
if (img != null) {
previewLabel.setIcon(new ImageIcon(img));
} else {
previewLabel.setIcon(null);
}
previewLabel.repaint();
}

void chooseColor() {
Color c = JColorChooser.showDialog(this, "Choose color",
cs.getReplaceColor());
if (c != null) {
cs.setReplaceColor(c);
}
}

void chooseImage() {
JFileChooser fc = new JFileChooser();
fc.addChoosableFileFilter(new SimpleFileFilter("Image files",
"*.png|*.jpg|*.gif|*.bmp", true));
int status = fc.showOpenDialog(this);
if (status == JFileChooser.APPROVE_OPTION) {
try {
URL url = fc.getSelectedFile().toURI().toURL();
cs.getMethod().addImageURL(url);
cs.setURL(url);
imageCombo.addItem(url);
} catch (MalformedURLException e) {
Logger.log.error("Unable to select image", e);
JOptionPane.showMessageDialog(this, "Unable to open image "
+ fc.getSelectedFile(), "Error",
JOptionPane.ERROR_MESSAGE);
}
imageCombo.setSelectedItem(cs.getURL());
}
}

@Override
public void actionPerformed(ActionEvent e) {
String action = e.getActionCommand();
if (ACTION_COMBO.equals(action)) {
cs.setColorSet(colorSetCombo.getSelectedColorSet());
} else if (ACTION_IMG.equals(action)) {
if (imageCheck.isSelected()) {
cs.setDefaultURL();
} else {
cs.setURL(null);
}
refreshImagePanel();
}
}
}

@Override
public void actionPerformed(ActionEvent e) {
String action = e.getActionCommand();
if (ACTION_BASIC.equals(action)) {
basic.refresh();
cardLayout.show(settings, action);
}
}

 *//** Panel for editing colorByLine */
/*
class Basic extends JPanel implements ActionListener, ListDataListener {
private static final long serialVersionUID = 1L;

private final SortSampleCheckList sampleList;

ColorSetCombo colorSetCombo;

public Basic() {
setLayout(new FormLayout("4dlu, pref, 2dlu, fill:pref:grow, 4dlu",
"4dlu, pref:grow, 4dlu, pref, 4dlu"));

List<ISample> selected = method.getSelectedSamples();
for (ISample s : selected)
if (s == null)
throw new NullPointerException();
sampleList = new SortSampleCheckList(selected,
method.getGexManager());
sampleList.getList().addActionListener(this);
sampleList.getList().setActionCommand(ACTION_SAMPLE);
sampleList.getList().getModel().addListDataListener(this);

options.addActionListener(this);
options.setActionCommand(ACTION_OPTIONS);

CellConstraints cc = new CellConstraints();
add(sampleList, cc.xyw(2, 2, 3));
add(options, cc.xy(2, 4));
refresh();

}

void refresh() {
sampleList.getList()
.setSelectedSamples(method.getSelectedSamples());
}

private void refreshSamples() {
List<ConfiguredSample> csamples = new ArrayList<ConfiguredSample>();
for (ISample s : sampleList.getList().getSelectedSamplesInOrder()) {
ConfiguredSample cs = method.new ConfiguredSample(s);
cs.setColorSet(method.getSingleColorSet());
csamples.add(cs);
}
method.setUseSamples(csamples);
}

@Override
public void actionPerformed(ActionEvent e) {
String action = e.getActionCommand();
if (ACTION_SAMPLE.equals(action)) {
refreshSamples();
method.setSingleColorSet(new ColorSet(csm));
} else if (ACTION_OPTIONS.equals(action)) {
OkCancelDialog optionsDlg = new OkCancelDialog(null,
ACTION_OPTIONS, (Component) e.getSource(), true, false);
optionsDlg.setDialogComponent(createAppearancePanel());
optionsDlg.pack();
optionsDlg.setVisible(true);
}
}

 *//**
 * Creates subpanel to set colours for positive and negative data and
 * minimum and maximum line thickness to create a line thickness
 * gradient
 * 
 * @return
 */
/*
public JPanel createAppearancePanel() {
new JPanel();
new JLabel("Expression:");
new JLabel("Color of line : ");
new JLabel("Rule Expression");
new JLabel("Color Gradient");
final JLabel poscolorLabel = new JLabel("Positive data");
final JLabel negcolorLabel = new JLabel("Negative data");
final JLabel thicknessLabel = new JLabel("Thickness Gradient : ");
new JLabel("Minimum thickness");
new JLabel("Maximum thickness");
final String[] gradientStrings = { "1", "2", "3", "4", "5", "6",
		"7", "8", "9", "10" };

final JButton poscolorButton = new JButton("...");
final JButton negcolorButton = new JButton("...");
final JButton ruleTrueClrButton = new JButton("...");
final JButton ruleFalseClrButton = new JButton("...");
final JButton mincolorButton = new JButton("...");
final JButton meancolorButton = new JButton("...");
final JButton maxcolorButton = new JButton("...");

final JComboBox minthicknessbox = new JComboBox(gradientStrings);
final JComboBox maxthicknessbox = new JComboBox(gradientStrings);

final JTextField rulexp = new JTextField(10);
rulexp.setEditable(true);
final JTextField minvalueclr = new JTextField(5);
minvalueclr.setEditable(true);
final JTextField meanvalueclr = new JTextField(5);
meanvalueclr.setEditable(true);
final JTextField maxvalueclr = new JTextField(5);
maxvalueclr.setEditable(true);

final JTextField minvalue = new JTextField(5);
minvalue.setEditable(true);
final JTextField maxvalue = new JTextField(5);
maxvalue.setEditable(true);

 *//**
 * Assigns default colours and values
 */
/*
ruleTrueClrButton.setOpaque(true);
ruleTrueClrButton.setForeground(Color.GREEN);
ruleTrueClrButton.setBackground(Color.GREEN);
ruleFalseClrButton.setOpaque(true);
ruleFalseClrButton.setForeground(Color.RED);
ruleFalseClrButton.setBackground(Color.RED);
mincolorButton.setOpaque(true);
mincolorButton.setForeground(Color.BLUE);
mincolorButton.setBackground(Color.BLUE);
meancolorButton.setOpaque(true);
meancolorButton.setForeground(Color.BLACK);
meancolorButton.setBackground(Color.BLACK);
maxcolorButton.setOpaque(true);
maxcolorButton.setForeground(Color.YELLOW);
maxcolorButton.setBackground(Color.YELLOW);

poscolorButton.setOpaque(true);
poscolorButton.setForeground(Color.GREEN);
poscolorButton.setBackground(Color.GREEN);
negcolorButton.setOpaque(true);
negcolorButton.setForeground(Color.RED);
negcolorButton.setBackground(Color.RED);

minthicknessbox.setSelectedIndex(0);
maxthicknessbox.setSelectedIndex(7);

minvalueclr.setText("1");
meanvalueclr.setText("0");
maxvalueclr.setText("5");
minvalue.setText("1");
maxvalue.setText("5");

 *//**
 * Action Listeners for buttons, combo boxes and text boxes to set
 * colours, linethicknesses and values to be used for the
 * visualization
 */
/*
 * mincolorButton.addActionListener(new ActionListener() {
 * 
 * @Override public void actionPerformed(ActionEvent e) { Color minColor =
 * JColorChooser.showDialog(mincolorButton, "Choose", Color.BLUE);
 * mincolorButton.setForeground(minColor);
 * mincolorButton.setBackground(minColor); method.setMinColor(minColor); } });
 * 
 * meancolorButton.addActionListener(new ActionListener() {
 * 
 * @Override public void actionPerformed(ActionEvent e) { Color meanColor =
 * JColorChooser.showDialog(meancolorButton, "Choose", Color.BLACK);
 * meancolorButton.setForeground(meanColor);
 * meancolorButton.setBackground(meanColor); method.setMinColor(meanColor); }
 * });
 * 
 * maxcolorButton.addActionListener(new ActionListener() {
 * 
 * @Override public void actionPerformed(ActionEvent e) { Color maxColor =
 * JColorChooser.showDialog(maxcolorButton, "Choose", Color.YELLOW);
 * maxcolorButton.setForeground(maxColor);
 * maxcolorButton.setBackground(maxColor); method.setMaxColor(maxColor); } });
 * 
 * poscolorButton.addActionListener(new ActionListener() {
 * 
 * @Override public void actionPerformed(ActionEvent e) { Color posColor =
 * JColorChooser.showDialog(poscolorButton, "Choose", Color.GREEN);
 * poscolorButton.setForeground(posColor);
 * poscolorButton.setBackground(posColor); method.setPosColor(posColor); } });
 * 
 * negcolorButton.addActionListener(new ActionListener() {
 * 
 * @Override public void actionPerformed(ActionEvent e) { Color negColor =
 * JColorChooser.showDialog(negcolorButton, "Choose", Color.RED);
 * negcolorButton.setForeground(negColor);
 * negcolorButton.setBackground(negColor); method.setNegColor(negColor); } });
 * 
 * minthicknessbox.addActionListener(new ActionListener() {
 * 
 * @Override public void actionPerformed(ActionEvent e) { JComboBox min =
 * (JComboBox) e.getSource(); String minthickness = (String)
 * min.getSelectedItem(); method.setMinThickness(minthickness); } });
 * 
 * maxthicknessbox.addActionListener(new ActionListener() {
 * 
 * @Override public void actionPerformed(ActionEvent e) { JComboBox max =
 * (JComboBox) e.getSource(); String maxthickness = (String)
 * max.getSelectedItem(); method.setMaxThickness(maxthickness); } });
 * minvalueclr.addActionListener(new ActionListener() {
 * 
 * @Override public void actionPerformed(ActionEvent e) { String maxdata =
 * maxvalue.getText(); method.setMaxData(maxdata); } });
 * 
 * meanvalueclr.addActionListener(new ActionListener() {
 * 
 * @Override public void actionPerformed(ActionEvent e) { String maxdata =
 * maxvalue.getText(); method.setMaxData(maxdata); } });
 * 
 * maxvalueclr.addActionListener(new ActionListener() {
 * 
 * @Override public void actionPerformed(ActionEvent e) { String mindata =
 * minvalue.getText(); method.setMinData(mindata); } });
 * 
 * rulexp.addActionListener(new ActionListener() {
 * 
 * @Override public void actionPerformed(ActionEvent e) { String rulexpression =
 * rulexp.getText(); method.setRulExp(rulexpression); } });
 * 
 * maxvalue.addActionListener(new ActionListener() {
 * 
 * @Override public void actionPerformed(ActionEvent e) { String maxdata =
 * maxvalue.getText(); method.setMaxData(maxdata); } });
 * 
 * minvalue.addActionListener(new ActionListener() {
 * 
 * @Override public void actionPerformed(ActionEvent e) { String mindata =
 * minvalue.getText(); method.setMinData(mindata); } }); DefaultFormBuilder
 * builder = new DefaultFormBuilder(new FormLayout(
 * "pref, 4dlu, fill:pref:grow, 4dlu, pref", ""));
 * builder.setDefaultDialogBorder();
 * builder.append("Data Visualization on Interactions"); builder.nextLine(); //
 * builder.addSeparator("Color Rule"); builder.append(new JLabel("Criteria:"),
 * rulexp); builder.nextLine(); builder.append(new JLabel("Met:"),
 * ruleTrueClrButton); builder.nextLine(); builder.append(new
 * JLabel("Not met:"), ruleFalseClrButton); builder.nextLine();
 * builder.append(new JLabel("Color Gradient")); builder.nextLine();
 * builder.append(mincolorButton, minvalueclr); builder.nextLine();
 * builder.append(meancolorButton, meanvalueclr); builder.nextLine();
 * builder.append(maxcolorButton, maxvalueclr); builder.nextLine();
 * builder.append(poscolorLabel, poscolorButton); builder.nextLine();
 * builder.append(negcolorLabel, negcolorButton); builder.nextLine();
 * builder.append(thicknessLabel); builder.nextLine(); builder.append(new
 * JLabel("Min:"), minthicknessbox, minvalue); builder.nextLine();
 * builder.append(new JLabel("Max:"), maxthicknessbox, maxvalue); return
 * builder.getPanel(); }
 * 
 * @Override public void contentsChanged(ListDataEvent e) { refreshSamples(); }
 * 
 * @Override public void intervalAdded(ListDataEvent e) { refreshSamples(); }
 * 
 * @Override public void intervalRemoved(ListDataEvent e) { refreshSamples(); }
 * }
 * 
 * }
 */