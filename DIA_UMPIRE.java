/*
*
* GENERAL INFO & DIA_UMPIRE SIGNAL EXTRACTION
* "private" unecessary?**
**/

GUI DIA_UMP = new GUI();
new DIA_UMP.setVisible(true);

class DIA_UMP{
import java.util.*;
import java.util.Scanner;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileWriter;
import java.io.BufferedWriter;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import java.awt.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

	public class GetInput extends JPanel {
	private static final long serialVersionUID = -6211781418045572320L;
	private static final String APP_TITLE = "Get Input";
	private static final int APP_WIDTH = 1;
	private static final int APP_HEIGHT = 1;
	private static JFrame frame;

/* How the buttons should be laid out (number of buttons on each row and column) */
	private JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
	private JPanel buttonLayer1 = new JPanel(new GridLayout(1,5));
	private JPanel buttonLayer2 = new JPanel(new GridLayout(1,2));


/* Create the main labels */
	private JLabel dia_UmpireLabel_Text = new JLabel("                    DIA_UMPIRE");

	private JLabel[] mainLabels =
				{ dia_UmpireLabel_Text };

/* Initialization of textfields, sets defaults to quoted text */
	private JTextField inputDir_Text = new JTextField("C:\\Users\\");
	private JTextField outputDir_Text = new JTextField("C:\\Users\\");
	private JTextField numOfThreads_Text = new JTextField("56");
	private JTextField amountOfRam_Text = new JTextField("128");
	private JTextField ab_Sciex_Ms_ConverterDir_Text = new JTextField("DEPRECIATED_DO_NOT_CHANGE");
	private JTextField msconvertDir_Text = new JTextField("C:\\Program Files\\ProteoWizard\\" + 
		                                               "ProteoWizard 3.0.9844\\msconvert.exe");
	private JTextField indexmzXMLDir_Text = new JTextField("C:\\Inetpub\\tpp-bin\\indexmzXML.exe");
	private JTextField dia_Umpire_SeJarDir_Text = new JTextField("C:\\DIA-Umpire_v2_0\\" +
		                                                               "DIA_Umpire_SE.jar");
	private JTextField mzxmlToMgfParams_Text = new JTextField("C:\\DIA-Umpire_v2_0\\" + 
		                                                              "Kac.se_params");

/* Array declaring JText fields initialized above  */
	private JTextField[] textFields = 
				{ 
				  inputDir_Text, outputDir_Text, numOfThreads_Text, amountOfRam_Text,
				  ab_Sciex_Ms_ConverterDir_Text, msconvertDir_Text, indexmzXMLDir_Text,
				  dia_Umpire_SeJarDir_Text
				};

/* Size for text field */
	private int textFieldWidth = 250;
	private int textFieldHeight = 20;

/* Check box for General Info */
	private JCheckBox DiaUmpire_Checkbox = new JCheckBox("                        Click to run  ");
	private JCheckBox[] checkBoxFields = 
					{ 
					  DiaUmpire_Checkbox
					};

/* Sets initial checkbox as empty */
	private boolean runDiaUmpire = false;

/* To prevent multiple save/load use parameters */
	private boolean saveParamsClosed = true;
	private boolean loadParamsClosed = true;

// OPTIONS FOR checkIfFilesExist? (line 195)*

/* A string buffer to hold all the data in the JTextFields */
	private StringBuffer fieldData = new StringBuffer();

/* A string buffer that contains all the errors that were found to report to the user */
	private StringBuffer errors = new StringBuffer();

/* A string buffer that will contain the data in the taxonomy file */
	private StringBuffer dataInTaxonomyFile = new StringBuffer();

/* A string buffer that will contain the checkbox matrix as a list of 1's and 0's */
	private StringBuffer checkboxStringBuf = new StringBuffer();

/* A StringBuffer that will contain user specified parameters to load */
	private StringBuffer parametersStringBuf = new StringBuffer();

/* the appWidth and appHeight */
    private int appWidth;
    private int appHeight;


/* A component Map that will contain all the elements being added to the panel */
    private Map<String, GridItem> componentMap;


/* Using a GridBagLayout */
    private GridBagLayout layout;
    private GridBagConstraints constraints;

/*  */
    private static class GridItem {
    private JComponent component;
    private boolean isExportable;
    private int xPos;
    private int yPos;
    private int colSpan;
    private int rowSpan;



/*
*
*
*/
    public GridItem(JComponent component, boolean isExportable, int xPos, int yPos) {
            this(component, isExportable, xPos, yPos, 1, 1);
        }



/*
*
*
*/
    public GridItem(JComponent component, boolean isExportable, int xPos, int yPos,
        				int colSpan, int rowSpan) {
            this.component = component;
            this.isExportable = isExportable;
            this.xPos = xPos;
            this.yPos = yPos;
            this.colSpan = colSpan;
            this.rowSpan = rowSpan;
        }
    }

    protected void init() {
/* Set the default information for the applet */
        this.constraints = new GridBagConstraints();
        this.layout = new GridBagLayout();
        this.componentMap = new LinkedHashMap<String, GridItem>();
        this.setLayout(this.layout);
        this.constraints.ipadx = 5;
        this.constraints.ipady = 5;
        this.constraints.insets = new Insets(1, 4, 1, 4);
        this.constraints.anchor = GridBagConstraints.LAST_LINE_START;
        this.constraints.fill = GridBagConstraints.HORIZONTAL;

/* Create and add all the components to the frame */
        addAllItems();

/*
*
*
*/
        (doneButton).addActionListener(new ActionListener() {
            @Override
    public void actionPerformed(ActionEvent e) {

        /* Create a boolean to check if any errors occur when cheching each input */
        int justReturn = 0;
		int errorOccurred = 1;
		int returnVal;

		returnVal = checkSelectedSections();

		/* Just return. Errors were already displayed in checkSelectedSections() */
		if( returnVal == justReturn ) {
			return;
			}
				
		/* Display the errors in a pop-up message dialog if any were encountered */
		if( returnVal == errorOccurred ) {
			JOptionPane.showMessageDialog(null, errors.toString());
			}
		/* 
		 * Else, display the message below if the Prophet or XTandem checkboxes were
		 * checked, grab the field data, write it to the input.parameters file,
		 * and close the program
		 */
		else {
			if(runProphet || runXTandem) {
				JOptionPane.showMessageDialog(null, "Errors may occur if incorrect " +
							                       "modification masses\nor cleavage sites " +
							                              "were set in the _params.xml file");
				}

				grabFieldData();
				writeToFile();
				System.exit(0);
				}
            }
        });;
/* End of ActionPerformed method */


 /* Sets the information below into the JTextBoxes when the Default JButton is clicked */

    (defaultButton).addActionListener(new ActionListener() {
        @Override
    public void actionPerformed(ActionEvent e) {

/* 
 * An array containing the name of the JTextBox, two colons, and the information
 * to set when the Default JButton is clicked
 */
        String[] lines = {
            "inputDirText::C:\\",
            "outputDirText::C:\\",
            "numOfThreadsText::56",
            "amountOfRamText::128",
			"AB_SCIEX_MS_ConverterDirText::DEPRECIATED_DO_NOT_CHANGE",
			"msconvertDirText::C:\\Program Files\\ProteoWizard\\ProteoWizard 3.0.9844\\" +
					                                                              "msconvert.exe",
			"indexmzXMLDirText::C:\\Inetpub\\tpp-bin\\indexmzXML.exe",
			"DIA_Umpire_SE_Jar_DirText::C:\\DIA-Umpire_v2_0\\DIA_Umpire_SE.jar",

            /* Calls setFieldData to put the default information in the JTextFields */
            setFieldData(lines);
            }
        });;
/* End of ActionPerformed method */


    (clearButton).addActionListener(new ActionListener() {
        @Override
    public void actionPerformed(ActionEvent e) {

            	/* Calls clearFieldData to clear all the field data */
                clearFieldData();

            }
        });;


        /*
        *
        *
        */
	(browseButton).addActionListener(new ActionListener() {
        @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println(lastTextFieldSelected);
        JFileChooser fileChooser = new JFileChooser("Computer");

        // For Directory
        //fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
 
        // For File
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        int rVal = fileChooser.showOpenDialog(null);

        if (rVal == JFileChooser.APPROVE_OPTION) {
        	lastTextFieldSelected.setText(fileChooser.getSelectedFile().toString());
        	}
        }
        });

/*
 *
 *
 */
    (loadButton).addActionListener(new ActionListener() {
        @Override
    public void actionPerformed(ActionEvent e) {
        if(loadParamsClosed) {
        	createLoadUserParams();
        		}
        	}
        });

/*
 *
 *
 */
    (saveButton).addActionListener(new ActionListener() {
        @Override
    public void actionPerformed(ActionEvent e) {
        if(saveParamsClosed) {
        	createSaveUserParams();
        		}
        	}
        });


/*
 *
 *
 */
	(exitButton).addActionListener(new ActionListener() {
        @Override
    public void actionPerformed(ActionEvent e) {
        /* Set all the JCheckBox booleans to false */
		runDiaUmpire = false;

		/* Grab the data in the JTextFields */
		grabFieldData();

		/* Write the information grabbed to the input.parameters file */
		writeToFile();

		/* Exit the program */
        System.exit(0);
            }
        });;

/*
 *
 *
 */
	(runMapDiaButton).addActionListener(new ActionListener() {
        @Override
    public void actionPerformed(ActionEvent e) {
        if(mapDIAClosed) {

            /*if(runOnce == 0) {
            	JOptionPane.showMessageDialog(null, "Enter General Info correctly before continuing");
            	runOnce++;
            	return;
       } */

        	String mapDiaLabelsStr = mapDIALabels_Text.getText() + ",";
            	mapDiaLabelsStr = mapDiaLabelsStr.replaceAll("\\s", "");

        	if(mapDiaLabelsStr.equals(",")) {
            	JOptionPane.showMessageDialog(null, "No labels were entered.");
            		return;
            			}

        	ArrayList<String> labelsList = new ArrayList<String>();
        		for(String retval : mapDiaLabelsStr.split(",")) {
       				labelsList.add(retval);
					}

            createMapDIAInput(labelsList.size(), labelsList);
            	}
        	}
        });;

/*
 *
 *
 */
	((JCheckBox) componentMap.get("DiaUmpireCheckbox").component).addItemListener(
		                                                          new ItemListener() {
    	@Override
    	public void itemStateChanged(ItemEvent e) {

		/* 
		 * If the DiaUmpireCheckbox is checked, set the respective boolean to true and
		 * enable the respective JTextFields
		 */
            if(e.getStateChange() == ItemEvent.SELECTED) {
				runDiaUmpire = true;
				ab_Sciex_Ms_ConverterDir_Text.setEnabled(true);
				msconvertDir_Text.setEnabled(true);
				indexmzXMLDir_Text.setEnabled(true);
				dia_Umpire_SeJarDir_Text.setEnabled(true);
				mzxmlToMgfParams_Text.setEnabled(true);
					}

		/* 
		 * If the DiaUmpireCheckbox is unchecked, set the respective boolean to false and
		 * disable the respective JTextFields
		 */
			else {
				runDiaUmpire = false;
				ab_Sciex_Ms_ConverterDir_Text.setEnabled(false);
				msconvertDir_Text.setEnabled(false);
				indexmzXMLDir_Text.setEnabled(false);
				dia_Umpire_SeJarDir_Text.setEnabled(false);
				mzxmlToMgfParams_Text.setEnabled(false);
			}

			/* End of itemStateChanged method */
            }
        });;
	}

/*
 * Creates and adds all the components to the frame
 *
 */
    public void addAllItems() {
    	int verticalPosition = 0;
        int horizontalPosition = 0;
		
		/* 
		 * Make all the JTextFields the same size and disable them. Add a FocusListener to all the
		 * JTextField to keep track of which one was the last one to be clicked on
		 */
		for( JTextField element : textFields ) {
			element.setPreferredSize( new Dimension(textFieldWidth, textFieldHeight) );
			element.setEnabled(false);

		(element).addFocusListener(new FocusListener() {
        	@Override
    	public void focusGained(FocusEvent e) {
            //System.out.println(e.getComponent().getClass().getName() + " focus gained");
            lastTextFieldSelected = (JTextField) e.getComponent();
            	}

        	@Override
    	public void focusLost(FocusEvent e) {
            //System.out.println("inputDir_Text focus lost");
            }
        	});;
		}

	/* Place the text on the left side of the JCheckBoxes */
		for( JCheckBox element : checkBoxFields ) {
			element.setHorizontalTextPosition(SwingConstants.LEFT);
		}
		
	/* Make all the main labels of the font ROMAN_BASELINE, bold, and size 24 */
		for( JLabel element : mainLabels ) {
			element.setFont(new Font("ROMAN_BASELINE", Font.BOLD, 24));
		}


	/* Place the labels in their appropriate locations */
		componentMap.put("dia_UmpireLabel", new GridItem(dia_UmpireLabel_Text, false,
			                            0, 0, 2, 1));

	/* Create the general info section and enable the text boxes here */
		verticalPosition = 1;
		horizontalPosition = 0;
		componentMap.put("generalInfo", new GridItem(new JLabel("General Info"), false,
			                            horizontalPosition+1, verticalPosition, 1, 1));
        componentMap.put("inputDir", new GridItem(new JLabel("<html>Enter input directory<br>" +
        	      "(only wiff files)</html>)"), false, horizontalPosition, ++verticalPosition));
        componentMap.put("inputDirText", new GridItem(inputDir_Text, true,
        	               horizontalPosition+1, verticalPosition, 1, 1));
        componentMap.put("outputDir", new GridItem(new JLabel("<html>Enter output directory<br>" +
        	         "(all other files)</html>"), false, horizontalPosition, ++verticalPosition));
        componentMap.put("outputDirText", new GridItem(outputDir_Text, true,
        	                 horizontalPosition+1, verticalPosition, 1, 1));
        componentMap.put("numOfThreads", new GridItem(new JLabel("Enter number of threads"), false,
        	                                              horizontalPosition, ++verticalPosition));
        componentMap.put("numOfThreadsText", new GridItem(numOfThreads_Text, true,
        	                       horizontalPosition+1, verticalPosition, 1, 1));
        componentMap.put("amountOfRam", new GridItem(new JLabel("Enter amount of ram"), false,
        	                                         horizontalPosition, ++verticalPosition));
        componentMap.put("amountOfRamText", new GridItem(amountOfRam_Text, true,
        	                     horizontalPosition+1, verticalPosition, 1, 1));
		inputDir_Text.setEnabled(true);
		outputDir_Text.setEnabled(true);
		numOfThreads_Text.setEnabled(true);
		amountOfRam_Text.setEnabled(true);
		
	/* Create the diaUmpire_pipe.py info section */
		verticalPosition = 7;
		horizontalPosition = 0;
		componentMap.put("diaUmpire_pipe_input", new GridItem(new JLabel("File conversions and DIA-Umpire Signal Extraction"),
			                                 false, horizontalPosition+1, verticalPosition, 1, 1));
        componentMap.put("AB_SCIEX_MS_Converter_Dir", new GridItem(new JLabel("<html>Path to<br>AB_SCIEX_MS_Converter.exe</html>"), false,
                                                         horizontalPosition, ++verticalPosition));
        componentMap.put("AB_SCIEX_MS_ConverterDirText", new GridItem(ab_Sciex_Ms_ConverterDir_Text,
                                               true, horizontalPosition+1, verticalPosition, 1, 1));
        componentMap.put("msconvertDir", new GridItem(new JLabel("<html>Path to " +
        	        "msconvert.exe</html>"), false, horizontalPosition, ++verticalPosition));
        componentMap.put("msconvertDirText", new GridItem(msconvertDir_Text, true,
        	                       horizontalPosition+1, verticalPosition, 1, 1));
        componentMap.put("indexmzXMLDir", new GridItem(new JLabel("<html>Path to " + 
        	        "indexmzXML.exe</html>"), false, horizontalPosition, ++verticalPosition));
        componentMap.put("indexmzXMLDirText", new GridItem(indexmzXMLDir_Text, true,
        	                         horizontalPosition+1, verticalPosition, 1, 1));
        componentMap.put("DIA_Umpire_SE_Jar_Dir", new GridItem(new JLabel("<html>Path to DIA_Umpire_SE.jar</html>"), false,
                                                          horizontalPosition, ++verticalPosition));
        componentMap.put("DIA_Umpire_SE_Jar_DirText", new GridItem(dia_Umpire_SeJarDir_Text, true,
                                                     horizontalPosition+1, verticalPosition, 1, 1));
		componentMap.put("mzxmlToMgfParams", new GridItem(new JLabel("<html>Path to DIA-Umpire<br>" +
		                                          "signal extraction .params</html>"), false,
		                                                   horizontalPosition, ++verticalPosition));
        componentMap.put("mzxmlToMgfParamsText", new GridItem(mzxmlToMgfParams_Text, true,
        	                               horizontalPosition+1, verticalPosition, 1, 1));

	/* Check box for DIA_UMPIRE */
		componentMap.put("DiaUmpireCheckbox", new GridItem(DiaUmpire_Checkbox, false, 0, 7));

	/* Bottom buttons for browsing, clearing, loading, saving... */
		buttonLayer1.add(clearButton);
		buttonLayer1.add(defaultButton);
		buttonLayer1.add(browseButton);
		buttonLayer1.add(loadButton);
		buttonLayer1.add(saveButton);
		buttonLayer2.add(exitButton);
		buttonLayer2.add(doneButton);

		buttonPanel.add(buttonLayer1);
		buttonPanel.add(buttonLayer2);

		componentMap.put("button_Panel", new GridItem(buttonPanel, false, 2, 19, 4, 1));

    }


/*
*
*
*/

    public int checkSelectedSections() {
    /* Create a boolean to check if any errors occur when cheching each input */
	   boolean errorOccurred = false;

    /* Clear the errors and fieldData string buffers*/
	   errors.delete(0, errors.length());
	   fieldData.delete(0, fieldData.length());
				

    /* Checking if the General Info inputs are filled out */
	   if(inputDir_Text.getText().equals("") || outputDir_Text.getText().equals("") || 
				                                      numOfThreads_Text.getText().equals("") ||
				                                       amountOfRam_Text.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "         Fill in all the fields");
		  return 0;
		}

    /* Checking if the General Info files entered exist */
	   if(!checkIfFilesExist(generalInfoOpt)) {
		  errorOccurred = true;
				}
				
        /* If the diaUmpire_pipe checkbox was checked */
	   if(runDiaUmpire) {

	   /* Checking if the diaUmpire_pipe.py Info inputs are filled out */
		if(msconvertDir_Text.getText().equals("") || mzxmlToMgfParams_Text.equals("") ||
					                                      indexmzXMLDir_Text.getText().equals("") ||
					                              dia_Umpire_SeJarDir_Text.getText().equals("") ||
					                           ab_Sciex_Ms_ConverterDir_Text.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "         Fill in all the fields");
			return 0;
			}

			/* Checking if the diaUmpire_pipe.py Info files entered exist */
		if(!checkIfFilesExist(diaUmpireOpt)) {
			errorOccurred = true;
				}
			}
		
		if( errorOccurred ) {
			return 1;
		}
		else {
			return 2;
		}

		
    /*
     *
     *
     */
    public void createMapDIAInput( int sizeOfMatrix, ArrayList<String> mapDiaLabels ) {

        JCheckBox[][] matrix = new JCheckBox[sizeOfMatrix][sizeOfMatrix];
        int[][] matrixInt = new int[sizeOfMatrix][sizeOfMatrix];

        mapDIAClosed = false;

        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                //int sizeOfMatrix = 3;


                JFrame frame = new JFrame("CheckBox Matrix");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel pane = new JPanel(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();

                c.ipadx = 5;
                c.ipady = 5;
                c.insets = new Insets(1, 4, 1, 4);
                //c.anchor = GridBagConstraints.LAST_LINE_START;
                //c.fill = GridBagConstraints.HORIZONTAL;

                for( int x = 1; x <= sizeOfMatrix; x++ ) {
                    c.gridx = x;
                    c.gridy = 0;
                    pane.add(new JLabel(""+mapDiaLabels.get(x-1)), c);
                }

                for( int y = 1; y <= sizeOfMatrix; y++ ) {
                    c.gridx = 0;
                    c.gridy = y;
                    pane.add(new JLabel(""+mapDiaLabels.get(y-1)), c);
                }

                for(int x = 0; x < sizeOfMatrix; x++) {
                    for(int y = 0; y < sizeOfMatrix; y++) {
                        matrixInt[x][y] = 0;
                    }
                }

                for(int yLoc = 0; yLoc < sizeOfMatrix; yLoc++ ) {
                    for(int xLoc = 0; xLoc < sizeOfMatrix; xLoc++ ) {
                        c.gridx = xLoc+1;
                        c.gridy = yLoc+1;
                        matrix[yLoc][xLoc] = new JCheckBox();

                        matrix[yLoc][xLoc].addItemListener( new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent item) {
                                checkboxStringBuf.delete(0, checkboxStringBuf.length());

                                if(item.getStateChange() == ItemEvent.SELECTED) {
                                    JCheckBox source = (JCheckBox) item.getSource();
                                    boolean selected = source.isSelected();
                                    int cbRow = -1;
                                    int cbCol = -1;
                                    for (int r = 0; r < matrix.length; r++) {
                                        for (int c = 0; c < matrix[r].length; c++) {
                                            if (source.equals(matrix[r][c])) {
                                                cbRow = r;
                                                cbCol = c;
                                            }
                                        }
                                    }

                                    matrixInt[cbCol][cbRow] = 1;

                                    //System.out.println("row: " + (cbRow+1) + " column: " + (cbCol+1));
                                }
                                else {
                                    JCheckBox source = (JCheckBox) item.getSource();
                                    boolean selected = source.isSelected();
                                    int cbRow = -1;
                                    int cbCol = -1;
                                    for (int r = 0; r < matrix.length; r++) {
                                        for (int c = 0; c < matrix[r].length; c++) {
                                            if (source.equals(matrix[r][c])) {
                                                cbRow = r;
                                                cbCol = c;
                                            }
                                        }
                                    }

                                    matrixInt[cbCol][cbRow] = 0;

                                    //System.out.println("delete: row: " + (cbRow+1) + " column: " + (cbCol+1));
                                }

                                for(int y = 0; y < sizeOfMatrix; y++) {
                                    for(int x = 0; x < sizeOfMatrix; x++) {
                                        if(x == y) {
                                            checkboxStringBuf.append("-");
                                        }
                                        else {
                                            checkboxStringBuf.append(matrixInt[x][y]);
                                        }

                                        if(x != sizeOfMatrix -1) {
                                            checkboxStringBuf.append(" ");
                                        }
                                    }
                                    checkboxStringBuf.append("\r\n");
                                }

                                //System.out.println(checkboxStringBuf.toString());
                                
                            }
                        });

                        pane.add(matrix[yLoc][xLoc], c);
                        if(xLoc == yLoc) {
                            matrix[yLoc][xLoc].setEnabled(false);
                        }
                    }
                }


                JPanel northPane = new JPanel(new GridBagLayout());
                GridBagConstraints otherConstraints = new GridBagConstraints();
                otherConstraints.anchor = GridBagConstraints.LAST_LINE_START;
                otherConstraints.fill = GridBagConstraints.HORIZONTAL;
                otherConstraints.ipadx = 5;
                otherConstraints.ipady = 5;
                otherConstraints.insets = new Insets(1, 4, 1, 4);

                int x = 0;
                int y = 0;
                otherConstraints.gridx = 0;
                otherConstraints.gridy = y;
                northPane.add(new JLabel("mapDIA Input = "), otherConstraints);
                otherConstraints.gridx = 1;
                otherConstraints.gridy = y;
                mapDIA_input_Text.setPreferredSize( new Dimension(textFieldWidth, textFieldHeight) );
                northPane.add(mapDIA_input_Text, otherConstraints);
                
                (finishedButton).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean error = false;
                        StringBuffer errorBuf = new StringBuffer();

                        if(checkboxStringBuf.toString().replaceAll("-", "").replaceAll("\r\n", "")
                                            .replaceAll("0", "").replaceAll(" ", "").equals("")) {
                            errorBuf.append("You did not select any comparison checkboxes\r\n");
                            error = true;
                        }

                        if(error == true) {
                            JOptionPane.showMessageDialog(null, errorBuf.toString());
                            errorBuf.delete(0, errorBuf.length());
                            return;
                        }
                    }

    
    /*
     *
     *
     */
    public void createLoadUserParams() {
        JButton user1 = new JButton("Load 1");
        JButton user2 = new JButton("Load 2");
        JButton user3 = new JButton("Load 3");
        JButton user4 = new JButton("Load 4");

        loadParamsClosed = false;

        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame frame = new JFrame("Load User Params");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel pane = new JPanel(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();

                c.ipadx = 10;
                c.ipady = 10;
                c.insets = new Insets(10, 4, 10, 4);

                c.gridx = 0;
                c.gridy = 0;
                pane.add(user1, c);

                c.gridx = 0;
                c.gridy = 1;
                pane.add(user2, c);

                c.gridx = 0;
                c.gridy = 2;
                pane.add(user3, c);

                c.gridx = 0;
                c.gridy = 3;
                pane.add(user4, c);

                (user1).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File user1Settings = new File(System.getProperty("user.dir") + "//userParams//user1.parameters");
                        readFile(user1Settings);
                        loadFile();
                        frame.dispose();
                        loadParamsClosed = true;
                    }
                });;

                (user2).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File user2Settings = new File(System.getProperty("user.dir") + "//userParams//user2.parameters");
                        readFile(user2Settings);
                        loadFile();
                        frame.dispose();
                        loadParamsClosed = true;
                    }
                });;

                (user3).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File user3Settings = new File(System.getProperty("user.dir") + "//userParams//user3.parameters");
                        readFile(user3Settings);
                        loadFile();
                        frame.dispose();
                        loadParamsClosed = true;
                    }
                });;

                (user4).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File user4Settings = new File(System.getProperty("user.dir") + "//userParams//user4.parameters");
                        readFile(user4Settings);
                        loadFile();
                        frame.dispose();
                        loadParamsClosed = true;
                    }
                });;

                frame.getContentPane().add(BorderLayout.CENTER, pane);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                frame.setResizable(false);

                frame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        loadParamsClosed = true;
                    }
                });
            }
        });
    }


    /*
     *
     *
     */
    public boolean readFile( File file ) {
        parametersStringBuf.delete(0, parametersStringBuf.length());

        Scanner fileToRead = null;
        try {
            fileToRead = new Scanner( file );
            String line;
            while( fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null ) {
                parametersStringBuf.append(line);
                parametersStringBuf.append("\r\n");
            }
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            return false;
        } finally {
            fileToRead.close();
            return true;
        }
    }



    /*
     *
     *
     */
    public void loadFile() {
        ArrayList<String> parametersList = new ArrayList<String>();

        for(String retval : parametersStringBuf.toString().split("\r\n")) {
            parametersList.add(retval);
        }

        String [] parametersArray = new String[parametersList.size()];

        for(int index = 0; index < parametersList.size() ; index++) {
            parametersArray[index] = parametersList.get(index);
        }

        setFieldData(parametersArray);
    }



    /*
     *
     *
     */
    public void createSaveUserParams() {
        JButton user1 = new JButton("Save 1");
        JButton user2 = new JButton("Save 2");
        JButton user3 = new JButton("Save 3");
        JButton user4 = new JButton("Save 4");

        saveParamsClosed = false;

        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame frame = new JFrame("Save User Parameters");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel pane = new JPanel(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();

                c.ipadx = 10;
                c.ipady = 10;
                c.insets = new Insets(10, 4, 10, 4);

                c.gridx = 0;
                c.gridy = 0;
                pane.add(user1, c);

                c.gridx = 0;
                c.gridy = 1;
                pane.add(user2, c);

                c.gridx = 0;
                c.gridy = 2;
                pane.add(user3, c);

                c.gridx = 0;
                c.gridy = 3;
                pane.add(user4, c);

                (user1).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File user1Settings = new File(System.getProperty("user.dir") + "//userParams//user1.parameters");
                        grabFieldData();
                        saveFile(user1Settings, fieldData);
                        frame.dispose();
                        saveParamsClosed = true;
                    }
                });;

                (user2).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File user2Settings = new File(System.getProperty("user.dir") + "//userParams//user2.parameters");
                        grabFieldData();
                        saveFile(user2Settings, fieldData);
                        frame.dispose();
                        saveParamsClosed = true;
                    }
                });;

                (user3).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File user3Settings = new File(System.getProperty("user.dir") + "//userParams//user3.parameters");
                        grabFieldData();
                        saveFile(user3Settings, fieldData);
                        frame.dispose();
                        saveParamsClosed = true;
                    }
                });;

                (user4).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File user4Settings = new File(System.getProperty("user.dir") + "//userParams//user4.parameters");
                        grabFieldData();
                        saveFile(user4Settings, fieldData);
                        frame.dispose();
                        saveParamsClosed = true;
                    }
                });;

                frame.getContentPane().add(BorderLayout.CENTER, pane);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                frame.setResizable(false);

                frame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        saveParamsClosed = true;
                    }
                });
            }
        });
    }



    /*
     *
     *
     */
    public void saveFile(File saveLocation, StringBuffer dataToSave) {
        ArrayList<String> parametersList = new ArrayList<String>();
        StringBuffer saveBuffer = new StringBuffer();
        int index = 1;
        int justReturn = 0;
        int errorOccurred = 1;
        int returnVal;

        saveBuffer.append(dataToSave.toString());

        returnVal = checkSelectedSections();

        if( returnVal == justReturn ) {
            return;
        }
                
        /* Display the errors in a pop-up message dialog if any were encountered */
        if( returnVal == errorOccurred ) {
            JOptionPane.showMessageDialog(null, errors.toString());
            return;
        }

        for(String retval : saveBuffer.toString().split("\r\n")) {
            parametersList.add(retval);
        }

        saveBuffer.delete(0, saveBuffer.length());

        saveBuffer.append("inputDirText::" + parametersList.get(index++)).append("\r\n");
        saveBuffer.append("outputDirText::" + parametersList.get(index++)).append("\r\n");
        saveBuffer.append("numOfThreadsText::" + parametersList.get(index++)).append("\r\n");
        saveBuffer.append("amountOfRamText::" + parametersList.get(index++)).append("\r\n");
        saveBuffer.append("AB_SCIEX_MS_ConverterDirText::" + parametersList.get(index++)).append("\r\n");
        saveBuffer.append("msconvertDirText::" + parametersList.get(index++)).append("\r\n");
        saveBuffer.append("indexmzXMLDirText::" + parametersList.get(index++)).append("\r\n");
        saveBuffer.append("DIA_Umpire_SE_Jar_DirText::" + parametersList.get(index++)).append("\r\n");
        saveBuffer.append("mzxmlToMgfParamsText::" + parametersList.get(index++)).append("\r\n");

        writeToFile(saveLocation, saveBuffer);
    }



    /*
     * Adds the components to the map
     *
     */
    protected void createChildren() {
        Iterator<Map.Entry<String, GridItem>> it;

        for (it = componentMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, GridItem> item = it.next();
            GridItem gridItem = item.getValue();

            this.constraints.gridx = gridItem.xPos;
            this.constraints.gridy = gridItem.yPos;
            this.constraints.gridwidth = gridItem.colSpan;
            this.constraints.gridheight = gridItem.rowSpan;

            this.add(gridItem.component, this.constraints);
        }
    }



    /*
     * Checks if the files exist. Only checks the JTextFields
     * 
     */
    private boolean checkIfFilesExist(int option) {

        /* Will be set to false if any file paths entered do not exist (false=errors encountered) */
        boolean filesExist = true;

        /* A path variable to temporarily hold each path entered */
        Path tempPath;

        try{

            /* Checking the General Info JTextFields */
            if(option == generalInfoOpt) {

                /* Checking if the input directory exists */
                tempPath = Paths.get( inputDir_Text.getText() );
                if( !Files.exists(tempPath) ) {
                    errors.append("input directory does not exist\r\n");
                    filesExist = false;
                }
                
                /* Checking if the output directory exists */
                tempPath = Paths.get( outputDir_Text.getText() );
                if( !Files.exists(tempPath) ) {
                    errors.append("output directory does not exist\r\n");
                    filesExist = false;
                }
            }


            /* Checking the diaUmpire_pipe.py Info JTextFields */
            if(option == diaUmpireOpt) {

                /* Checking if AB_SCIEX_MS_Converter.exe exists */
                /*tempPath = Paths.get( ab_Sciex_Ms_ConverterDir_Text.getText() );
                if( !Files.exists(tempPath) ) {
                    errors.append("AB_SCIEX_MS_Converter.exe does not exist\r\n");
                    filesExist = false;
                }*/

                /* Checking if msconvert.exe exists */
                tempPath = Paths.get( msconvertDir_Text.getText() );
                if( !Files.exists(tempPath) ) {
                    errors.append("msconvert.exe does not exist\r\n");
                    filesExist = false;
                }
                
                /* Checking if indexMZXML.exe exists */
                tempPath = Paths.get( indexmzXMLDir_Text.getText() );
                if( !Files.exists(tempPath) ) {
                    errors.append("indexMZXML.exe does not exist\r\n");
                    filesExist = false;
                }
                
                /* Checking if DIA_Umpire_SE.jar exists */
                tempPath = Paths.get( dia_Umpire_SeJarDir_Text.getText() );
                if( !Files.exists(tempPath) ) {
                    errors.append("DIA_Umpire_SE.jar does not exist\r\n");
                    filesExist = false;
                }
                
                /* Checking if the .params file exists */
                tempPath = Paths.get( mzxmlToMgfParams_Text.getText() );
                if( !Files.exists(tempPath) ) {
                    errors.append(".params file does not exist\r\n");
                    filesExist = false;
                }
            }

        }

        /* 
         * If any of the paths were entered incorrectly (illegal characters, etc.), delete all the
         * errors that were found and display the message below
         */
        catch( Exception e ) {
            errors.delete(0, errors.length());
            errors.append("Some file paths have been entered incorrectly.\r\n");
            errors.append("Make sure the paths are in this format:\r\n");
            errors.append("          C:\\Inetpub\\tpp-bin\\sed.exe\r\n");
            errors.append("Also check the URL in the taxonomy file");
            filesExist = false;
        }
        
        /*
         * Returns true or false indicating if any errors were encountered
         * (false = errors encountered)
         */
        return filesExist;
    }




    /*
     *
     *
     */
    private void grabFieldData() {

        /* If the input or output directories end in a '\', delete that character */
        String inputDirSTR = inputDir_Text.getText();
        String outputDirSTR = outputDir_Text.getText();

        fieldData.delete(0, fieldData.length());

        if( inputDirSTR.lastIndexOf('\\') == inputDirSTR.length()-1 ) {
            inputDirSTR = inputDirSTR.substring(0, inputDirSTR.length()-1);
        }
        if( outputDirSTR.lastIndexOf('\\') == outputDirSTR.length()-1 ) {
            outputDirSTR = outputDirSTR.substring(0, outputDirSTR.length()-1);
        }
        
        /* Begin filling out the fieldData StringBuffer (will become the input.parameters file) */
        fieldData.append("Keep this line\r\n");
        fieldData.append(inputDirSTR).append("\r\n");
        fieldData.append(outputDirSTR).append("\r\n");
        fieldData.append(numOfThreads_Text.getText()).append("\r\n");
        fieldData.append(amountOfRam_Text.getText()).append("\r\n");
        
        /* Use the information entered if selected, else fill the StringBuffer with default info */
        if(runDiaUmpire) {
            fieldData.append(ab_Sciex_Ms_ConverterDir_Text.getText()).append("\r\n");
            fieldData.append(msconvertDir_Text.getText()).append("\r\n");
            fieldData.append(indexmzXMLDir_Text.getText()).append("\r\n");
            fieldData.append(dia_Umpire_SeJarDir_Text.getText()).append("\r\n");
            fieldData.append(mzxmlToMgfParams_Text.getText()).append("\r\n");
        }
        else {
            fieldData.append("C:\\").append("\r\n");
            fieldData.append("C:\\").append("\r\n");
            fieldData.append("C:\\").append("\r\n");
            fieldData.append("C:\\").append("\r\n");
            fieldData.append("C:\\").append("\r\n");
        }
    /* Add the JCheckBox booleans to the StringBuffer */
        fieldData.append(runDiaUmpire).append("\r\n");

    }

    /*
     *
     *
     */
    private void writeToFile() {

        /* 
         * Write the contents of the fieldData StringBuffer to the input.parameters file located in
         * the current directory
         */

        String inputPathString = Paths.get(".").toAbsolutePath().normalize().toString();
        inputPathString += "\\input.parameters";
        
        try {
            BufferedWriter bufwriter = new BufferedWriter( new FileWriter(inputPathString) );
            bufwriter.write(fieldData.toString());
            bufwriter.close();
        } catch( Exception e ) {
            e.printStackTrace();
        }
        
    }



    /*
     *
     *
     */
    private  void writeToFile( File file, StringBuffer buffer ) {
        try {
            BufferedWriter bufwriter = new BufferedWriter( new FileWriter(file.toString()) );
            bufwriter.write(buffer.toString());
            bufwriter.close();
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }


    /*
     *
     *
     */
    private  void writeToFile( String outFile, StringBuffer buffer ) {
        String inputPathString = Paths.get(".").toAbsolutePath().normalize().toString();
        inputPathString += "\\";
        inputPathString += outFile;
        try {
            BufferedWriter bufwriter = new BufferedWriter( new FileWriter(outFile) );
            bufwriter.write(buffer.toString());
            bufwriter.close();
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }

    


    /*
     *
     *
     */
    private void clearFieldData() {
        /* Clears all the JTextFields */
        for( JTextField field : textFields ) {
            field.setText("");
        }
        
        /* Resets the checkboxes and disables the appropriate JTextFields */
        resetTextAndCheckboxes();
    }



    /*
     *
     *
     */
    private void setFieldData(String[] textLines) {
        clearFieldData();

        for (String line : textLines) {
            String[] values = line.split("::");

            if (values.length == 2) {
                GridItem gridItem = componentMap.get(values[0]);

                if (gridItem.isExportable && gridItem.component instanceof JTextComponent) {
                    JTextComponent field = ((JTextComponent) gridItem.component);
                    field.setText(values[1]);
                    field.setCaretPosition(0);
                }
            }
        }
    }
    
    


    /*
     *
     *
     */
    private void resetTextAndCheckboxes() {
        /* Set all the textbox booleans to false */
        runDiaUmpire = false;

        /* Disable all the JTextFields */
        for( JTextField element : textFields ) {
            element.setEnabled(false);
        }
        
        /* Uncheck all the JCheckBoxes */
        for( JCheckBox element : checkBoxFields ) {
            element.setSelected(false);
        }
        
        /* Enable the general information JTextFields */
        inputDir_Text.setEnabled(true);
        outputDir_Text.setEnabled(true);
        numOfThreads_Text.setEnabled(true);
        amountOfRam_Text.setEnabled(true);
    }


    
    /*
     *
     *
     */
    public static void main(String[] args) {
        
        /* Sets the frame and runs the application */
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame(APP_TITLE);
                frame.setContentPane(new GetInput(APP_WIDTH, APP_HEIGHT));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setResizable(false);
            }
        });
    }
}
    