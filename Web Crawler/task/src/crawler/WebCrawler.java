package crawler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler extends JFrame {

    //TableUrlWebPage tableUrlWebPage;
    //JTable urlWebPage;

    //int depth = 0;
    CreatorWorkers workers;

    public WebCrawler() throws IOException {

        super("Web Crawler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        JPanel top = top();
        //JPanel body = body();
        //JPanel footer = footer();

        add(top, new GridBagConstraints(0, 0, 1, 1, 1 , 1, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(1,1,1,1),0,0));
        //add(body, new GridBagConstraints(0, 1, 1, 1, 1 , 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1,1,1,1),0,0));
        //add(footer, new GridBagConstraints(0, 2, 1, 1, 1 , 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(1,1,1,1),0,0));

    }

    JPanel top(){

        JPanel top = new JPanel();
        top.setLayout(new GridBagLayout());

        JLabel urlTextLabel = new JLabel("Start URL: ");

        JTextField UrlTextField = new JTextField();
        UrlTextField.setName("UrlTextField");

        JToggleButton runButton = new JToggleButton("Run");
        runButton.setName("RunButton");

        JLabel workersTextLabel = new JLabel("Workers: ");

        JTextField workersTextField = new JTextField();
        workersTextField.setName("WorkersTextField");

        JLabel depthTextLabel = new JLabel("Maximum depth: ");

        JTextField depthTextField = new JTextField();
        depthTextField.setName("DepthTextField");

        JCheckBox depthCheck = new JCheckBox();
        depthCheck.setName("DepthCheckBox");

        JLabel depthEnableTextLabel = new JLabel("Enable");

        JLabel timeLimitTextLabel = new JLabel("Time limit: ");

        JTextField timeLimitTextField = new JTextField();
        timeLimitTextField.setName("WorkersTextField");

        JLabel timeLimitSecondsTextLabel = new JLabel("seconds");

        JCheckBox timeLimitCheck = new JCheckBox();
        timeLimitCheck.setName("TimeLimitCheckBox");

        JLabel timeLimitEnableTextLabel = new JLabel("Enable");

        JLabel elapsedTimeTextLabel = new JLabel("Elapsed time: ");

        JLabel timerTextLabel = new JLabel();

        JLabel parsedPagesTextLabel = new JLabel("Parsed pages: ");

        JLabel parsedCPagesTextLabel = new JLabel();
        parsedCPagesTextLabel.setName("ParsedLabel");

        JLabel exportTextLabel = new JLabel("Export: ");

        JTextField exportTextField = new JTextField();
        exportTextField.setName("ExportUrlTextField");

        JButton exportButton = new JButton("Save");
        exportButton.setName("ExportButton");
//-----
        UrlTextField.setPreferredSize(new Dimension(200,30));

        runButton.setSize(50,30);

        workersTextField.setPreferredSize(new Dimension(200,30));

        depthTextField.setPreferredSize(new Dimension(200,30));

        timeLimitTextField.setPreferredSize(new Dimension(200,30));

        exportTextField.setPreferredSize(new Dimension(200,30));

        exportButton.setSize(50,30);

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(5,5,5,5);

        c.gridx = 0;
        c.gridy = 0;
        top.add(urlTextLabel, c);
        c.gridx = 1;
        top.add(UrlTextField, c);
        c.gridx = 2;
        top.add(runButton, c);
        c.gridx = 0;
        c.gridy = 1;
        top.add(workersTextLabel, c);
        c.gridx = 1;
        top.add(workersTextField, c);
        c.gridx = 0;
        c.gridy = 2;
        top.add(depthTextLabel, c);
        c.gridx = 1;
        top.add(depthTextField, c);
        c.gridx = 2;
        top.add(depthCheck, c);
        c.gridx = 3;
        top.add(depthEnableTextLabel, c);
        c.gridx = 0;
        c.gridy = 3;
        top.add(timeLimitTextLabel, c);
        c.gridx = 1;
        top.add(timeLimitTextField, c);
        c.gridx = 2;
        top.add(timeLimitSecondsTextLabel, c);
        c.gridx = 3;
        top.add(timeLimitCheck, c);
        c.gridx = 4;
        top.add(timeLimitEnableTextLabel, c);
        c.gridx = 0;
        c.gridy = 4;
        top.add(elapsedTimeTextLabel, c);
        c.gridx = 1;
        top.add(timerTextLabel, c);
        c.gridx = 0;
        c.gridy = 5;
        top.add(parsedPagesTextLabel, c);
        c.gridx = 0;
        c.gridy = 6;
        top.add(parsedCPagesTextLabel, c);
        c.gridx = 1;
        top.add(exportTextLabel, c);
        c.gridx = 1;
        top.add(exportTextField, c);
        c.gridx = 2;
        top.add(exportButton, c);



        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //System.out.println(depthCheck.);
                if(workers == null) {
                    workers = new CreatorWorkers(depthCheck.isSelected() ? Integer.parseInt(depthTextField.getText()) : -1,
                            timeLimitCheck.isSelected() ? Integer.parseInt(timeLimitTextField.getText()) : -1,
                            Integer.parseInt(workersTextField.getText()),
                            timerTextLabel,
                            parsedCPagesTextLabel);
                    workers.getUrls().add(new MyData(0, UrlTextField.getText()));
                }
                if(workers.getStatus() == 0){
                    workers.timer.start();
                    try {
                        workers.createWorkers();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runButton.setText("Stop");
                }else if(workers.getStatus() == 1){
                    workers.timer.stop();
                    System.out.println("Stop");
                    workers.stop();
                    runButton.setText("Run");
                }
                //update(t);
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println(exportTextField.getText());
                if(workers != null){
                  workers.exportFile(exportTextField.getText());
                }

            }
        });


        return top;
    }

    JPanel body(){

        JPanel textWebPage = new JPanel();
        textWebPage.setLayout(new GridBagLayout());

        //tableUrlWebPage = new TableUrlWebPage();
        //urlWebPage = urlWebPage(tableUrlWebPage);
        //JScrollPane scrollTextAreaWebPage = new JScrollPane(urlWebPage);
        //scrollTextAreaWebPage.setPreferredSize(new Dimension(600,400));

        //textWebPage.add(scrollTextAreaWebPage,new GridBagConstraints(0, 0, 1, 1, 1 , 1, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(1,1,1,1),0,0));

        return textWebPage;
    }

    JPanel footer() throws IOException {
        JPanel footer = new JPanel();
        footer.setLayout(new GridBagLayout());

        JLabel exportLabel = new JLabel("Export: ");
        exportLabel.setName("ExportLabel");

        JTextField textFieldInputExport = new JTextField();
        textFieldInputExport.setName("ExportUrlTextField");

        JButton exportButton = new JButton("Save");
        exportButton.setName("ExportButton");

        textFieldInputExport.setPreferredSize(new Dimension(200,30));
        exportButton.setSize(50,30);

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(5,5,5,5);

        c.gridx = 0;
        c.gridy = 0;
        footer.add(exportLabel, c);

        c.gridx = 1;
        footer.add(textFieldInputExport, c);

        c.gridx = 2;
        footer.add(exportButton, c);

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println(textFieldInputExport.getText());
               // exportFile(textFieldInputExport.getText());
            }
        });

        return footer;
    }

    JTable urlWebPage(TableUrlWebPage tableUrlWebPage){

        JTable urlWebPage = new JTable(tableUrlWebPage);
        urlWebPage.setName("TitlesTable");
        urlWebPage.setEnabled(false);

        return urlWebPage;
    }

    String searchTitleLabel(String webPage){
        Matcher matcher = Pattern.compile(
                "<title>.+</title>"
        ).matcher(webPage);

        if (matcher.find()) {
            return matcher.group().replaceAll("</?title>", "");
        } else {
            return "";
        }
    }







}
