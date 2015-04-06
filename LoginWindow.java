/**
 * CS 460: Secure Communication
 * Professor: Ting Ting Chen
 *
 * Final Project
 *
 * <LoginWindow>
 *
 * Madhav Chhura
 */
package edu.csupomona.cs460.final_project;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Madhav Chhura 
 *
 */
public class LoginWindow {
    JFrame frame;
    JPanel panel;
    JLabel userLabel, passwordLabel;
    JTextField userText;
    JPasswordField passwordText;
    JButton loginButton, registerButton;
    UserDatabase database;
    
    /**
     * Initializes a frame and panel used to create a LoginWindow.
     * 
     * @pre true
     * @post an instance of LoginWindow and UserDatabe is created.
     */
    LoginWindow(){
        frame = new JFrame("CS 460 Final Project");
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);
        
        database = new UserDatabase();
        frame.setVisible(true);
    }
    /**
     * Places components on to the JPanel.
     * Labels, buttons and textfields are created.
     */

    private void placeComponents(JPanel panel) {

        panel.setLayout(null);

        userLabel = new JLabel("Username");
        userLabel.setBounds(10, 10, 80, 25);
        panel.add(userLabel);

        userText = new JTextField();
        userText.setBounds(100, 10, 160, 25);
        panel.add(userText);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 40, 80, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBounds(100, 40, 160, 25);
        panel.add(passwordText);

        loginButton = new JButton("login");
        loginButton.setBounds(10, 80, 80, 25);
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(userText.getText().toString().length() <= 0 || passwordText.getPassword().length <= 0){
                    JOptionPane.showMessageDialog(frame, 
                            "Please enter Username & Password to login!", 
                            "CS460",
                            JOptionPane.INFORMATION_MESSAGE);
                }else
                    loginUser(userText.getText().toString(), passwordText.getPassword());
            }
        });
        panel.add(loginButton); 
        
        registerButton = new JButton("register");
        registerButton.setBounds(180, 80, 80, 25);
        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(userText.getText().toString().length() <= 0 || passwordText.getPassword().length <= 0){
                    JOptionPane.showMessageDialog(frame, 
                            "Please enter Username & Password to register!", 
                            "CS460",
                            JOptionPane.INFORMATION_MESSAGE);
                }else
                    registerUser(userText.getText().toString(), passwordText.getPassword());
            }  
        });
        panel.add(registerButton);
    }
    /**
     * Called when a user presses the login button. 
     * The method sends the appropriate fields to the @class UserDatabase to 
     * check if the user can login with entered credentials.
     * @param username name of the user which is using to login with.
     * @param password password which user entered to login with.
     */
    private void loginUser(String userName, char[] password){
        String pw = new String(password);
        if(database.checkUser(userName)){
            if(!database.login(userName, pw)){
                JOptionPane.showMessageDialog(frame, 
                        "Password does not match! Try Again", 
                            "Invalid Password",
                            JOptionPane.ERROR_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(frame, 
                        "Welcome " + userName + "",
                        "Login Succesfull!",
                            JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            } 
        }
        else{
            JOptionPane.showMessageDialog(frame, 
                    "Username Does Not Exist!", 
                    "Invalid Username",
                    JOptionPane.ERROR_MESSAGE);
        }
    }   
    /**
     * Called when a user presses the register button. 
     * The method sends the appropriate fields to the @class UserDatabase to 
     * register the user in the server.
     * @param username name the user is trying to register in the program.
     * @param password password the user is trying to register in the program.
     */
    private void registerUser(String userName, char[] password){
        
        String pw = new String (password);
        if(!database.addUser(userName, pw)){
            JOptionPane.showMessageDialog(frame, 
                    "Username Already Taken - Pick Another UserName", 
                    "Username Already Taken",
                    JOptionPane.ERROR_MESSAGE);
        }   
    }
    /**
     * Called when the program is started. 
     * 
     */
    public static void main(String []args){
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
               new LoginWindow();
            }
        });
    }

}

