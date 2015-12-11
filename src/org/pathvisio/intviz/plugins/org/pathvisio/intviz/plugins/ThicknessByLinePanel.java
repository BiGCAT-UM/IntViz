// PathVisio,
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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.pathvisio.data.DataException;
import org.pathvisio.data.ISample;
import org.pathvisio.desktop.visualization.ColorSet;
import org.pathvisio.desktop.visualization.ColorSetManager;
import org.pathvisio.gui.dialogs.OkCancelDialog;
import org.pathvisio.intviz.plugins.ThicknessByLine.ConfiguredSample;
import org.pathvisio.visualization.gui.ColorSetCombo;
import org.pathvisio.visualization.plugins.SortSampleCheckList;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Configuration panel for the ColorByLine visualization method
 * 
 * @author anwesha
 */
public class ThicknessByLinePanel extends JPanel implements ActionListener,
		ListDataListener {
	private static final long serialVersionUID = 1L;

	static final String ACTION_SAMPLE = "sample";
	static final String ACTION_OPTIONS = "Thickness Gradient Options";
	static final String ACTION_COMBO = "Colorset";

	private final ThicknessByLine method;
	private final ColorSetManager csm;
	private final SortSampleCheckList sampleList;
	 ButtonGroup btnGrp = new ButtonGroup();
	ColorSetCombo colorSetCombo;

	JButton options = new JButton("Change Thickness Gradient");

	public ThicknessByLinePanel(ThicknessByLine method, ColorSetManager csm) {
		this.method = method;
		this.csm = csm;
		setLayout(new FormLayout("4dlu, pref, 2dlu, fill:pref:grow, 4dlu",
				"4dlu, pref:grow, 4dlu, pref, 4dlu"));
		final CellConstraints cc = new CellConstraints();
		
		JPanel samplePane = new JPanel();
		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(
				"pref, 4dlu, fill:pref:grow, 4dlu, pref", ""));
		builder.setDefaultDialogBorder();
		
		
		
		final List<ISample> selected = method.getSelectedSamples();
		for (final ISample s : selected)
			if (s == null)
				throw new NullPointerException();
		
		try {
			
			 List<String> samples = method.getGexManager().getCurrentGex().getSampleNames();
			 for(String sampleName : samples){
					JRadioButton btn = new JRadioButton(sampleName);
					btnGrp.add(btn);
					btn.setSelected(true);
					builder.append(btn);
				}
		} catch (DataException e) { 
			e.printStackTrace();
		}	
				 btnGrp.getSelection().addActionListener(this);
		 btnGrp.getSelection().setActionCommand(ACTION_SAMPLE);
//		 btnGrp.getElements().get
		 samplePane.add(builder.getPanel());
		 
		sampleList = new SortSampleCheckList(selected, method.getGexManager());
//		sampleList.getList().addActionListener(this);
//		sampleList.getList().setActionCommand(ACTION_SAMPLE);
//		sampleList.getList().getModel().addListDataListener(this);

		options.addActionListener(this);
		options.setActionCommand(ACTION_OPTIONS);
		
		add(samplePane, cc.xyw(2, 2, 3));
		add(options, cc.xy(2, 4));
		refresh();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final String action = e.getActionCommand();
//		 btnGrp.getSelection().addActionListener(this);
//		 btnGrp.getSelection().setActionCommand(ACTION_SAMPLE);
		if (ACTION_SAMPLE.equals(action)) {
			refreshSamples();
			method.setSingleColorSet(new ColorSet(csm));
		} else if (ACTION_OPTIONS.equals(action)) {
			final OkCancelDialog optionsDlg = new OkCancelDialog(null,
					ACTION_OPTIONS, (Component) e.getSource(), true, false);
			optionsDlg.setDialogComponent(createAppearancePanel());
			optionsDlg.pack();
			optionsDlg.setVisible(true);
		}
	}

	@Override
	public void contentsChanged(ListDataEvent e) {
		refreshSamples();
	}

	/**
	 * Creates subpanel to set colours for positive and negative data and
	 * minimum and maximum linethickness to create a line thickness gradient
	 * 
	 * @return
	 */
	public JPanel createAppearancePanel() {
		final JLabel minlabel = new JLabel("Minimum");
		final JLabel maxlabel = new JLabel("Maximum");
		final JLabel gap = new JLabel(" ");
		final JLabel thicknesslabel = new JLabel("Thickness");
		final JLabel datalabel = new JLabel("Data");
		final String[] gradientStrings = { "1", "2", "3", "4", "5", "6", "7",
				"8", "9", "10" };

		final JComboBox minthicknessbox = new JComboBox(gradientStrings);
		final JComboBox maxthicknessbox = new JComboBox(gradientStrings);

		final JTextField minvalue = new JTextField(5);
		minvalue.setEditable(true);
		final JTextField maxvalue = new JTextField(5);
		maxvalue.setEditable(true);

		/**
		 * Assigns default values
		 */
		minthicknessbox.setSelectedIndex(0);
		maxthicknessbox.setSelectedIndex(7);
		minvalue.setText("1");
		maxvalue.setText("5");

		/**
		 * Action Listeners for combo boxes and text boxes to set
		 * linethicknesses and values to be used for the visualization
		 */
		final DefaultFormBuilder builder = new DefaultFormBuilder(
				new FormLayout("pref, 4dlu, fill:pref:grow, 4dlu, pref", ""));
		builder.setDefaultDialogBorder();
		builder.append(gap, minlabel, maxlabel);
		builder.nextLine();
		builder.append(thicknesslabel, minthicknessbox, maxthicknessbox);
		builder.nextLine();
		builder.append(datalabel, minvalue, maxvalue);
		return builder.getPanel();
	}

	@Override
	public void intervalAdded(ListDataEvent e) {
		refreshSamples();
	}

	@Override
	public void intervalRemoved(ListDataEvent e) {
		refreshSamples();
	}

	void refresh() {
		sampleList.getList().setSelectedSamples(method.getSelectedSamples());
	}

	private void refreshSamples() {
		final List<ConfiguredSample> csamples = new ArrayList<ConfiguredSample>();
		for (final ISample s : sampleList.getList().getSelectedSamplesInOrder()) {
			final ConfiguredSample cs = method.new ConfiguredSample(s);
			cs.setColorSet(method.getSingleColorSet());
			csamples.add(cs);
		}
		method.setUseSamples(csamples);
	}
}
