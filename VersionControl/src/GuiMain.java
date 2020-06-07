import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class GuiMain extends JFrame{
	String basePath = null;
	
	public GuiMain() {
		JFrame jFrame = new JFrame("Ӣ��Ǩ�ƹ��� Version0.55");
		
		jFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setHgap(20);
		flowLayout.setVgap(20);
		
		JPanel jPanel = new JPanel(flowLayout);
		
		JLabel heroLabel = new JLabel("Ӣ������");
		//heroLabel.setBounds(1, 1, 50, 50);
		jPanel.add(heroLabel);
		
		JTextField heroText = new JTextField(5);
		heroText.setBounds(100, 1, 300, 50);
		jPanel.add(heroText);
		
		jFrame.add(jPanel);
		
		JLabel srcVersionLabel = new JLabel("Ǩ�ư汾");
		jPanel.add(srcVersionLabel);
		
		String[] srcVersion = {"0_5", "0_5_6", "tw_1_1", "jp_1_0"};
		JComboBox<String> srcComboBox = new JComboBox<>(srcVersion);
		JList<String> srcList = new JList<>(srcVersion);
		JScrollPane srcPane = new JScrollPane(srcList);
		
		jPanel.add(srcComboBox);
		
		JLabel dstVersionLabel = new JLabel("Ŀ��汾");
		jPanel.add(dstVersionLabel);
		
		String[] dstVersion = {"0_5_6", "tw_1_1" , "kr_1_1", "us_1_0", "sea_1_0", "jp_1_0", "jp_1_1"};
		JComboBox<String> dstComboBox = new JComboBox<>(dstVersion);
		JList<String> dstList = new JList<>(dstVersion);
		JScrollPane dstPane = new JScrollPane(dstList);
		
		jPanel.add(dstComboBox);
		
		JButton fileChooseButton = new JButton("ѡ�����·��");
		jPanel.add(fileChooseButton);
		
		fileChooseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jFileChooser.setCurrentDirectory(new File("E:"));
				//jFileChooser.setCurrentDirectory(new File("C:\\Users\\zhezheng.wang\\eclipse-workspace\\ParseList"));
				
				int value = jFileChooser.showOpenDialog(null);
				basePath = jFileChooser.getSelectedFile().getPath();
				if(value == JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(null, "����·����" + basePath);
				}
			}
		});
		
		
		JButton runButton = new JButton("����");
		runButton.setBounds(1, 1, 50, 50);
		jPanel.add(runButton);
		
		runButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String key = heroText.getText();
				String src = srcVersion[srcComboBox.getSelectedIndex()];
				String dst = dstVersion[dstComboBox.getSelectedIndex()];
				if(basePath == null) {
					JOptionPane.showMessageDialog(null, "����");
					System.exit(1);
				}
				if(key != null && !key.equals("")) {
					boolean success = Controller.move(src, dst, key, basePath);
					if(success == true) {
						JOptionPane.showMessageDialog(null, "�Ѿ����");
						System.exit(1);
					} else {
						JOptionPane.showMessageDialog(null, "�е����⣬������̨��");
					}
				}
				
				
			}
		});
		
		
		jFrame.setBounds(400, 300, 800, 100);
		jFrame.setVisible(true);
	}
	
	public static void main(String[] args){
		GuiMain guiMain = new GuiMain();
	}
}
