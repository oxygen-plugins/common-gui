package com.github.oxygenPlugins.common.gui.manuell;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.oxygenPlugins.common.gui.swing.SwingUtil;
import com.github.oxygenPlugins.common.gui.types.BooleanVerifier;
import com.github.oxygenPlugins.common.gui.types.CalendarTimeVerifier;
import com.github.oxygenPlugins.common.gui.types.CalendarVerifier;
import com.github.oxygenPlugins.common.gui.types.ColorVerifier;
import com.github.oxygenPlugins.common.gui.types.IntegerVerifier;
import com.github.oxygenPlugins.common.gui.types.LabelField;
import com.github.oxygenPlugins.common.gui.types.StringVerifier;
import com.github.oxygenPlugins.common.gui.types.TimeVerifier;
import com.github.oxygenPlugins.common.gui.types.VerifierFactory;
import com.github.oxygenPlugins.common.gui.types.converter.EnumTypeConverter;
import com.github.oxygenPlugins.common.gui.types.converter.TypeConverter;

public class LabelFieldTester {

	// typeVerifierMap.put("xs:string", new StringVerifier());
	// typeVerifierMap.put("xs:boolean", new BooleanVerifier());
	// typeVerifierMap.put("xs:int", new IntegerVerifier("","+-"));
	// typeVerifierMap.put("xs:integer", new IntegerVerifier("","+-"));
	// typeVerifierMap.put("xs:short", new IntegerVerifier("","+-"));
	// typeVerifierMap.put("xs:long", new IntegerVerifier("","+-"));
	// typeVerifierMap.put("xs:decimal", new IntegerVerifier(".","+-"));
	// typeVerifierMap.put("xs:unsignedInt", new IntegerVerifier("","+"));
	// typeVerifierMap.put("xs:unsignedShort", new IntegerVerifier("","+"));
	// typeVerifierMap.put("sqf:color", new ColorVerifier());
	// typeVerifierMap.put("xs:date", new CalendarVerifier());
	// typeVerifierMap.put("xs:dateTime", new CalendarTimeVerifier());
	// typeVerifierMap.put("xs:time", new TimeVerifier());

	public static void main(String[] args) {
		ViewTester vt = new ViewTester();
		
		
//		with Default
		LabelField stringWD = VerifierFactory.createEntryLabel(null, null, new TypeConverter("xs:string", "MyDefault"),
				vt);
		LabelField booleanWD = VerifierFactory.createEntryLabel(stringWD, null, new TypeConverter("xs:boolean", true),
				vt);
		LabelField intWD = VerifierFactory.createEntryLabel(booleanWD, null, new TypeConverter("xs:int", 1000), vt);
		LabelField integerWD = VerifierFactory.createEntryLabel(intWD, null, new TypeConverter("xs:integer", 500), vt);
		LabelField shortWD = VerifierFactory.createEntryLabel(integerWD, null, new TypeConverter("xs:short", 256), vt);
		LabelField longWD = VerifierFactory.createEntryLabel(shortWD, null, new TypeConverter("xs:long", 1024), vt);
		LabelField doubleWD = VerifierFactory.createEntryLabel(longWD, null, new TypeConverter("xs:double", 10.5), vt);
		LabelField unsignIntWD = VerifierFactory.createEntryLabel(doubleWD, null,
				new TypeConverter("xs:unsignedInt", 10), vt);
		LabelField dateWD = VerifierFactory.createEntryLabel(unsignIntWD, null,
				new TypeConverter("xs:date", "1986-04-05"), vt);
		LabelField dateTimeWD = VerifierFactory.createEntryLabel(dateWD, null,
				new TypeConverter("xs:dateTime", "2017-10-01T01:16:08.768"), vt);
		LabelField timeWD = VerifierFactory.createEntryLabel(dateTimeWD, null,
				new TypeConverter("xs:time", "01:16:08.768"), vt);
		
		// unsuported
		LabelField colorWD = VerifierFactory.createEntryLabel(unsignIntWD, null,
				new TypeConverter("sqf:color", "#000077"), vt);
		
//		without default
		LabelField stringWoD = VerifierFactory.createEntryLabel(null, null, new TypeConverter("xs:string", null),
				vt);
		LabelField booleanWoD = VerifierFactory.createEntryLabel(stringWD, null, new TypeConverter("xs:boolean", null),
				vt);
		LabelField integerWoD = VerifierFactory.createEntryLabel(intWD, null, new TypeConverter("xs:integer", null), vt);
		LabelField doubleWoD = VerifierFactory.createEntryLabel(longWD, null, new TypeConverter("xs:double", null), vt);
		LabelField dateWoD = VerifierFactory.createEntryLabel(unsignIntWD, null,
				new TypeConverter("xs:date", null), vt);
		LabelField timeWoD = VerifierFactory.createEntryLabel(dateTimeWD, null,
				new TypeConverter("xs:time", null), vt);


		EnumTypeConverter enumType = new EnumTypeConverter("xs:string", new String[]{"xs:string", "xs:integer", "xs:double"}, 1);

		LabelField multipleChoice = VerifierFactory.createEntryLabel(timeWoD, null, enumType, vt);

		LabelField[] fields = new LabelField[] { intWD, integerWD, shortWD, longWD, doubleWD,
				unsignIntWD, dateWD, timeWD, booleanWD, stringWD, stringWoD, timeWoD, booleanWoD, integerWoD, doubleWoD, dateWoD, multipleChoice};

		JFormattedTextField amountTextField = new JFormattedTextField(NumberFormat.getNumberInstance());
		amountTextField.setColumns(5);

		GridBagLayout gbl = new GridBagLayout();
		JPanel mainPanel = new JPanel(gbl);

		SwingUtil.addComponent(mainPanel, gbl, new JLabel("With default values"), 0, 0, 2, 1, 1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
		int i = 1;

		LabelField prevLF = null;

		for (final LabelField lf : fields) {
			String type = lf.getType().getType();
			lf.setMinimumSize(new Dimension(70, 20));
			lf.setBorder(BorderFactory.createLineBorder(Color.GRAY));

			lf.setPrevComponent(prevLF);

			lf.setFocusable(false);

			final JLabel typeLabel = new JLabel(type);

			typeLabel.setFocusable(true);

			typeLabel.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					typeLabel.setForeground(Color.BLACK);
				}

				@Override
				public void focusGained(FocusEvent e) {
					typeLabel.setForeground(Color.BLUE);
				}
			});
			typeLabel.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent ke) {
				}

				@Override
				public void keyReleased(KeyEvent ke) {

					int kechar = ke.getKeyCode();
					switch (kechar) {
					
					case KeyEvent.VK_ENTER:
					case KeyEvent.VK_ESCAPE:
						break;
//						typeLabel.transferFocus();
//						break;
//					case KeyEvent.VK_UP:
//						typeLabel.transferFocusBackward();
//						break;
					case KeyEvent.VK_DOWN:
						typeLabel.transferFocus();
						break;
					case KeyEvent.VK_UP:
						typeLabel.transferFocusBackward();
						break;
					case KeyEvent.VK_RIGHT:
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_F2:
						lf.activateVerifier();
						break;
					default:
						break;
					}
				}

				@Override
				public void keyPressed(KeyEvent ke) {
					int kecode = ke.getKeyCode();
					switch (kecode) {
					default:
						break;
					}
				}
			});

			SwingUtil.addComponent(mainPanel, gbl, typeLabel, 0, i, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
					GridBagConstraints.HORIZONTAL);
			SwingUtil.addComponent(mainPanel, gbl, lf, 1, i++, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
					GridBagConstraints.NONE);

			prevLF = lf;

		}

		SwingUtil.addComponent(mainPanel, gbl, amountTextField, 1, i++, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE);

		// SwingUtil.addComponent(mainPanel, gbl, new JLabel("xs:boolean"), 0,
		// 2, 1, 1, 1.0, 0.0,
		// GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
		// SwingUtil.addComponent(mainPanel, gbl, booleanWD, 1, 2, 1, 1, 0.0,
		// 0.0, GridBagConstraints.NORTHWEST,
		// GridBagConstraints.NONE);
		//
		// SwingUtil.addComponent(mainPanel, gbl, new JLabel("xs:int"), 0, 3, 1,
		// 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
		// GridBagConstraints.HORIZONTAL);
		// SwingUtil.addComponent(mainPanel, gbl, intWD, 1, 3, 1, 1, 0.0, 0.0,
		// GridBagConstraints.NORTHWEST,
		// GridBagConstraints.NONE);
		//
		// SwingUtil.addComponent(mainPanel, gbl, new JLabel("xs:integer"), 0,
		// 4, 1, 1, 1.0, 0.0,
		// GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
		// SwingUtil.addComponent(mainPanel, gbl, integerWD, 1, 4, 1, 1, 0.0,
		// 0.0, GridBagConstraints.NORTHWEST,
		// GridBagConstraints.NONE);
		//
		// SwingUtil.addComponent(mainPanel, gbl, new JLabel("xs:short"), 0, 5,
		// 1, 1, 1.0, 0.0,
		// GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
		// SwingUtil.addComponent(mainPanel, gbl, shortWD, 1, 5, 1, 1, 0.0, 0.0,
		// GridBagConstraints.NORTHWEST,
		// GridBagConstraints.NONE);
		//
		// SwingUtil.addComponent(mainPanel, gbl, new JLabel("xs:long"), 0, 6,
		// 1, 1, 1.0, 0.0,
		// GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
		// SwingUtil.addComponent(mainPanel, gbl, longWD, 1, 6, 1, 1, 0.0, 0.0,
		// GridBagConstraints.NORTHWEST,
		// GridBagConstraints.NONE);
		//
		// SwingUtil.addComponent(mainPanel, gbl, new JLabel("xs:decimal"), 0,
		// 7, 1, 1, 1.0, 0.0,
		// GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
		// SwingUtil.addComponent(mainPanel, gbl, decimalWD, 1, 7, 1, 1, 0.0,
		// 0.0, GridBagConstraints.NORTHWEST,
		// GridBagConstraints.NONE);
		//
		// SwingUtil.addComponent(mainPanel, gbl, new JLabel("xs:unsignedInt"),
		// 0, 8, 1, 1, 1.0, 0.0,
		// GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
		// SwingUtil.addComponent(mainPanel, gbl, unsignIntWD, 1, 8, 1, 1, 0.0,
		// 0.0, GridBagConstraints.NORTHWEST,
		// GridBagConstraints.NONE);
		//
		// SwingUtil.addComponent(mainPanel, gbl, new JLabel("sqf:color"), 0, 9,
		// 1, 1, 1.0, 0.0,
		// GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
		// SwingUtil.addComponent(mainPanel, gbl, colorWD, 1, 9, 1, 1, 0.0, 0.0,
		// GridBagConstraints.NORTHWEST,
		// GridBagConstraints.NONE);
		//
		// SwingUtil.addComponent(mainPanel, gbl, new JLabel("xs:date"), 0, 10,
		// 1, 1, 1.0, 0.0,
		// GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
		// SwingUtil.addComponent(mainPanel, gbl, dateWD, 1, 10, 1, 1, 0.0, 0.0,
		// GridBagConstraints.NORTHWEST,
		// GridBagConstraints.NONE);
		//
		// SwingUtil.addComponent(mainPanel, gbl, new JLabel("xs:dateTime"), 0,
		// 11, 1, 1, 1.0, 0.0,
		// GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
		// SwingUtil.addComponent(mainPanel, gbl, dateTimeWD, 1, 11, 1, 1, 0.0,
		// 0.0, GridBagConstraints.NORTHWEST,
		// GridBagConstraints.NONE);
		//
		// SwingUtil.addComponent(mainPanel, gbl, new JLabel("xs:time"), 0, 12,
		// 1, 1, 1.0, 0.0,
		// GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
		// SwingUtil.addComponent(mainPanel, gbl, timeWD, 1, 12, 1, 1, 0.0, 0.0,
		// GridBagConstraints.NORTHWEST,
		// GridBagConstraints.NONE);

		vt.test(mainPanel);

	}
}
