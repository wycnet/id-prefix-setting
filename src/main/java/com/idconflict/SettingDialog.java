import com.alibaba.fastjson.JSON;
import com.idconflict.DataBean;
import com.idconflict.UserSettingValue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SettingDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton preMode;
    private JTextArea preStringList;



    public SettingDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setMinimumSize(new Dimension(400, 300));
        this.setLocationRelativeTo(getRootPane());

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        initView();
    }

    private void initView() {
        UserSettingValue usv = UserSettingValue.getInstance();
        preMode.setSelected(usv.isPre);
        StringBuilder sb = new StringBuilder();
        for (String s: usv.preStringList) {
            sb.append(s).append(System.getProperty("line.separator"));
        }
        preStringList.setText(sb.toString());
    }

    private void onOK() {
        // add your code here
        UserSettingValue sv = UserSettingValue.getInstance();
        sv.isPre = preMode.isSelected();
        String preTextString = preStringList.getText();
        String[] preTextList = preTextString.split(System.getProperty("line.separator"));
        sv.preStringList.clear();
        for (String s: preTextList) {
            if(s ==null || s.isBlank()){
                continue;
            }
            sv.preStringList.add(s);
        }

        try {
            String jsonString = JSON.toJSONString(new DataBean(sv.isPre, sv.preStringList));
            // 保存到本地
            UserSettingValue.saveStringToFile(UserSettingValue.savePath, jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        SettingDialog dialog = new SettingDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
