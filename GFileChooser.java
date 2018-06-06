package Compilador;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class GFileChooser {
	public String askForFile()
	{
		JFileChooser chooser = new JFileChooser();
		int approvedOption;
		do{
			approvedOption = chooser.showOpenDialog(null);
			if(approvedOption == JFileChooser.APPROVE_OPTION){
				return  chooser.getSelectedFile().getAbsolutePath();
			}
			else{
				JOptionPane.showMessageDialog(null, "You have to choose a file!");
			}
		}while(approvedOption != JFileChooser.APPROVE_OPTION);
		return null;
	}
	
	public String saveToFile(){
		JFileChooser chooser = new JFileChooser();
		int approvedOption;
		do{
			approvedOption = chooser.showSaveDialog(null);
			if(approvedOption == JFileChooser.APPROVE_OPTION){
				return  chooser.getSelectedFile().getAbsolutePath();
			}
			else{
				JOptionPane.showMessageDialog(null, "You have to choose a file!");
			}
		}while(approvedOption != JFileChooser.APPROVE_OPTION);
		return null;
	}
}