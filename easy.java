/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author ASUS
 */
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.swing.JOptionPane;

public class easy extends javax.swing.JFrame {
private int level = 1;
private int waktu = 30;
private int score = 0;
private javax.swing.Timer gameTimer;
private javax.swing.Timer moleTimer;
private javax.swing.JButton[] tombolMole;
private int moleIndex = -1;
private Clip clip;
private int highScore = 0;
private boolean isBomb = false;
    /**
     * Creates new form easy
     */
    public easy() {
         setTitle("GAME");
         initComponents();
         tombolMole = new javax.swing.JButton[]{btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9};
         playLoopingMusic();
         setCustomCursor();
         setLocationRelativeTo(null);   
    }
    private void playLoopingMusic() {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/image/music.wav");
            if (audioSrc == null) {
                System.err.println("File musik tidak ditemukan!");
                return;
            }

            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void dispose() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
        super.dispose();}
    
    private void playSoundEffect(String soundFile) {
    try {
        InputStream audioSrc = getClass().getResourceAsStream("/image/" + soundFile);
        if (audioSrc == null) {
            System.err.println("File sound effect tidak ditemukan: " + soundFile);
            return;
        }
        InputStream bufferedIn = new BufferedInputStream(audioSrc);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

        Clip clipEffect = AudioSystem.getClip();
        clipEffect.open(audioStream);
        clipEffect.start();
        clipEffect.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                clipEffect.close();
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private void setCustomCursor() {
    try {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image cursorImage = toolkit.getImage(getClass().getResource("/image/palu.png"));
        Point hotspot = new Point(0, 0);
        Cursor paluCursor = toolkit.createCustomCursor(cursorImage, hotspot, "Palu Cursor");
        
        for (javax.swing.JButton button : tombolMole) {
            button.setCursor(paluCursor);
        }
        
        mulai.setCursor(Cursor.getDefaultCursor());
        quit4.setCursor(Cursor.getDefaultCursor());
        
    } catch (Exception e) {
        System.err.println("Gagal load kursor custom: " + e.getMessage());
    }
}
    
 private void startGame() {
    waktu = 30;
    score = 0;
    labelScore.setText("SCORE : " + score);
    labelWaktu.setText("TIME : " + waktu);
    
    // Jika timer sebelumnya masih jalan, hentikan dulu
    if (gameTimer != null && gameTimer.isRunning()) {
        gameTimer.stop();
    }
    if (moleTimer != null && moleTimer.isRunning()) {
        moleTimer.stop();
    }

    gameTimer = new javax.swing.Timer(1000, e -> {
        waktu--;
        labelWaktu.setText("TIME : " + waktu);
        if (waktu <= 0) {
            gameTimer.stop();
            moleTimer.stop();
            tampilkanGameOver();
        }
    });
    
    int delay = 1500; 

    
    moleTimer = new javax.swing.Timer(delay, e -> {
        randomMole();
    });
    
    gameTimer.start();
    moleTimer.start();
}
    private void randomMole() {
        if (moleIndex != -1) {
            tombolMole[moleIndex].setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/hole1.png")));
        }

        moleIndex = (int) (Math.random() * 9);
        
        if (Math.random() < 0.4) {
            isBomb = true;
            java.net.URL bombURL = getClass().getResource("/image/bom.png");
            if (bombURL != null) {
                tombolMole[moleIndex].setIcon(new javax.swing.ImageIcon(bombURL));
            }
        } else {
            isBomb = false;
            java.net.URL moleURL = getClass().getResource("/image/mole1.png");
            if (moleURL != null) {
                tombolMole[moleIndex].setIcon(new javax.swing.ImageIcon(moleURL));
            }
        }
    }

    private void whackMole(int index) {
        if (index == moleIndex) {
            if (isBomb) {
                playSoundEffect("bom.wav");
                gameTimer.stop();
                moleTimer.stop();
                tampilkanGameOver();
            } else {
                playSoundEffect("hit.wav"); 
                score++;
                labelScore.setText("SCORE : " + score);
                tombolMole[moleIndex].setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/hole1.png")));
                moleIndex = -1;
                
                if (score > highScore) {
                    highScore = score;
                }
            }
        } else { 
            playSoundEffect("miss.wav");
        }
    }


private void tampilkanGameOver() {
    playSoundEffect("gameover.wav");
    if (score > highScore) {
        highScore = score;
    }
    
    Object[] options = {"Restart", "Main Menu"};
    int choice = JOptionPane.showOptionDialog(
        this,
        "GAME OVER\n\n" +
        "Your Score: " + score + "\n" +
        "High Score: " + highScore + "\n\n" +
        "Apa yang mau kamu lakukan?",
        "Game Over",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.INFORMATION_MESSAGE,
        null,
        options,
        options[1]
    );

    if (choice == JOptionPane.YES_OPTION) {
        startGame();
    } else if (choice == JOptionPane.NO_OPTION) {
        Start nextLevel = new Start();
        nextLevel.setVisible(true);
        this.dispose();
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        labelScore = new javax.swing.JLabel();
        labelWaktu = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btn1 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        btn5 = new javax.swing.JButton();
        btn6 = new javax.swing.JButton();
        btn7 = new javax.swing.JButton();
        btn8 = new javax.swing.JButton();
        btn9 = new javax.swing.JButton();
        quit4 = new javax.swing.JButton();
        mulai = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Snap ITC", 3, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 0, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Whack A Mole");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(262, 11, 340, 66);

        labelScore.setFont(new java.awt.Font("Snap ITC", 0, 24)); // NOI18N
        labelScore.setForeground(new java.awt.Color(204, 204, 255));
        labelScore.setText("Score : 0");
        jPanel1.add(labelScore);
        labelScore.setBounds(120, 100, 188, 42);

        labelWaktu.setFont(new java.awt.Font("Snap ITC", 0, 24)); // NOI18N
        labelWaktu.setForeground(new java.awt.Color(255, 204, 204));
        labelWaktu.setText("Time : 30");
        jPanel1.add(labelWaktu);
        labelWaktu.setBounds(590, 100, 221, 53);

        jPanel2.setLayout(new java.awt.GridLayout(3, 3));

        btn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/hole1.png"))); // NOI18N
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActionPerformed(evt);
            }
        });
        jPanel2.add(btn1);

        btn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/hole1.png"))); // NOI18N
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActionPerformed(evt);
            }
        });
        jPanel2.add(btn2);

        btn3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/hole1.png"))); // NOI18N
        btn3.setActionCommand("jButton3");
        btn3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3ActionPerformed(evt);
            }
        });
        jPanel2.add(btn3);

        btn4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/hole1.png"))); // NOI18N
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn4ActionPerformed(evt);
            }
        });
        jPanel2.add(btn4);

        btn5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/hole1.png"))); // NOI18N
        btn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn5ActionPerformed(evt);
            }
        });
        jPanel2.add(btn5);

        btn6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/hole1.png"))); // NOI18N
        btn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn6ActionPerformed(evt);
            }
        });
        jPanel2.add(btn6);

        btn7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/hole1.png"))); // NOI18N
        btn7.setAutoscrolls(true);
        btn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7ActionPerformed(evt);
            }
        });
        jPanel2.add(btn7);

        btn8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/hole1.png"))); // NOI18N
        btn8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn8ActionPerformed(evt);
            }
        });
        jPanel2.add(btn8);

        btn9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/hole1.png"))); // NOI18N
        btn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn9ActionPerformed(evt);
            }
        });
        jPanel2.add(btn9);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(110, 167, 705, 315);

        quit4.setBackground(new java.awt.Color(204, 0, 153));
        quit4.setFont(new java.awt.Font("Snap ITC", 0, 12)); // NOI18N
        quit4.setText("Back");
        quit4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quit4ActionPerformed(evt);
            }
        });
        jPanel1.add(quit4);
        quit4.setBounds(110, 494, 100, 40);

        mulai.setBackground(new java.awt.Color(102, 204, 255));
        mulai.setFont(new java.awt.Font("Snap ITC", 0, 12)); // NOI18N
        mulai.setText("Start");
        mulai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mulaiActionPerformed(evt);
            }
        });
        jPanel1.add(mulai);
        mulai.setBounds(701, 493, 110, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/rumput.png"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 0, 910, 540);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn9ActionPerformed
        whackMole(8); 
    }//GEN-LAST:event_btn9ActionPerformed

    private void btn8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn8ActionPerformed
         whackMole(7); 
    }//GEN-LAST:event_btn8ActionPerformed

    private void quit4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quit4ActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Confirm Quit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Start nextLevel = new Start();
            nextLevel.setVisible(true);

            // Tutup frame level sekarang
            this.dispose();
        }
    }//GEN-LAST:event_quit4ActionPerformed

    private void btn7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn7ActionPerformed
        whackMole(6); 
    }//GEN-LAST:event_btn7ActionPerformed

    private void btn6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn6ActionPerformed
        whackMole(5); 
    }//GEN-LAST:event_btn6ActionPerformed

    private void btn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn5ActionPerformed
         whackMole(4); 
    }//GEN-LAST:event_btn5ActionPerformed

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed
        whackMole(3); 
    }//GEN-LAST:event_btn4ActionPerformed

    private void btn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3ActionPerformed
         whackMole(2); 
    }//GEN-LAST:event_btn3ActionPerformed

    private void btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActionPerformed
         whackMole(1); 
    }//GEN-LAST:event_btn2ActionPerformed

    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed
         whackMole(0); 
    }//GEN-LAST:event_btn1ActionPerformed

    private void mulaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mulaiActionPerformed
         startGame(); 
    }//GEN-LAST:event_mulaiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(easy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(easy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(easy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(easy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new easy().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel labelScore;
    private javax.swing.JLabel labelWaktu;
    private javax.swing.JButton mulai;
    private javax.swing.JButton quit4;
    // End of variables declaration//GEN-END:variables
}
