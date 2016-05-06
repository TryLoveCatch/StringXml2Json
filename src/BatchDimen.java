
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BatchDimen extends JFrame {
    
    JPanel projectNamePanel = new JPanel();
    
    JPanel formerPathPanel;
    JTextField formerPathtf;
    JButton formerPathButton;
    
    JPanel desPathPanel;
    JTextField desPathtf;
    JButton desPathButton;
    
    
    
    JPanel generatePanel;
    JButton generateButton;
    JLabel tipLabel;
    
    String formerPath;
    String desPath;
    
    CoreManager mManager;
    
    public static void main(String[] args)
    {
        new BatchDimen().luanch();
    }
    
    
    public void luanch() {
        
        mManager = new CoreManager();
        mManager.setCallback(new CoreManager.Callback() {
			
			@Override
			public void onComplete() {
                tipLabel.setText("生成成功!!!");
//              try {
//                  Thread.sleep(1000);
//              } catch (InterruptedException e1) {
//                  e1.printStackTrace();
//              }
//              
//              System.exit(-1);
				
			}
		});
        
        setTitle(" -- xml 转 json --  TryLoveCatch荣誉出品 ");
        
        setBounds(300, 250, 520, 180);
        setDefaultCloseOperation(3);
        GridLayout gl = new GridLayout(3, 1);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(gl);
        
        
        this.formerPathPanel = new JPanel();
        this.formerPathPanel.setLayout(new FlowLayout(0));
        this.formerPathtf = new JTextField(30);
        this.formerPathtf.setEditable(false);
        this.formerPathButton = new JButton("请选择xml文件地址");
        
        this.formerPathButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser choser = new JFileChooser("C:\\");
                choser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = choser.showOpenDialog(BatchDimen.this);
                if (result == 0) {
                    File file = choser.getSelectedFile();
                    formerPath = file.getAbsolutePath();
                    formerPathtf.setText(formerPath);
                    
                    desPath = file.getParentFile().getAbsolutePath();
                    desPathtf.setText(desPath);
                    
                } else if (result == 1) {
                    formerPath = null;
                    formerPathtf.setText("你没有选取xml文件地址");
                }
            }
        });
        
        formerPathPanel.add(this.formerPathtf);
        formerPathPanel.add(this.formerPathButton);
        
        this.desPathPanel = new JPanel();
        this.desPathPanel.setLayout(new FlowLayout(0));
        this.desPathtf = new JTextField(30);
        this.desPathtf.setEditable(false);
        this.desPathButton = new JButton("请选择Json保存地址");
        this.desPathButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser choser = new JFileChooser("C:\\");
                choser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = choser.showOpenDialog(BatchDimen.this);
                if (result == 0) {
                    File file = choser.getSelectedFile();
                    desPath = file.getAbsolutePath();
                    desPathtf.setText(desPath);
                } else if (result == 1) {
                    desPath = null;
                    desPathtf.setText("你没有选取保存json文件地址");
                }
            }
        });
        
        desPathPanel.add(this.desPathtf);
        desPathPanel.add(this.desPathButton);
        
        
        this.generatePanel = new JPanel();
        this.generatePanel.setLayout(new FlowLayout(0));
        this.generateButton = new JButton("Go!!");
        this.tipLabel = new JLabel();
        
        this.generateButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                formerPath = formerPathtf.getText().trim();
                desPath = desPathtf.getText().trim();
                
                if ((formerPath == null) || (formerPath.trim().equals(""))) {
                    tipLabel.setText("请选择正确的原始目录!");
                    return;
                } else if ((desPath == null) || (desPath.trim().equals(""))) {
                    tipLabel.setText("请选择正确的目标目录!");
                    return;
                }
                mManager.startWork(formerPath, desPath);

            }
        });
        
        this.generatePanel.add(this.generateButton);
        this.generatePanel.add(this.tipLabel);
        
       
        mainPanel.add(this.formerPathPanel);
        mainPanel.add(this.desPathPanel);
        
        mainPanel.add(this.generatePanel);
        
        add(mainPanel, "Center");
        
        setResizable(false);

        setVisible(true);
        
    }
}
