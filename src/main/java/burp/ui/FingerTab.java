package burp.ui;

import burp.*;
import burp.model.FingerPrintRule;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashSet;
import java.util.HashMap;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.awt.Component;
import javax.swing.JTable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class FingerTab implements IMessageEditorController {
    public static JPanel contentPane;
    private JLabel lbHost;
    private JTextField tfHost;
    private JLabel lbPort;
    private JTextField tfPort;
    private JLabel lbTimeout;
    private JTextField tfTimeout;
    private JLabel lbIntervalTime;
    private JTextField tfIntervalTime;
    private JLabel lbUsername;
    private JTextField tfUsername;
    private JLabel lbPassword;
    private JTextField tfPassword;
    private JTextField tfDomain;
    private JTextField tfExcludeSuffix;
    private JTextField tfBlackList;
    private JToggleButton btnConn;
    private JButton btnClear;
    private JSplitPane splitPane;
    public static HttpLogTable logTable;
    public static IHttpRequestResponse currentlyDisplayedItem;
    public static JLabel lbRequestCount;
    public static JLabel lbSuccessCount;
    public static JLabel lbFailCount;

    public static IMessageEditor requestViewer;
    public static IMessageEditor responseViewer;
    public static ITextEditor resultDeViewer;

    public static HashMap<String, JLabel> resultMap = new HashMap<>();
    public static JPanel tagsPanel;

    // 菜单页面
    public static JMenuBar menuBar;
    public static JMenu menuMainPage;
    public static JMenu menuConfigPage;
    // 在FingerTab类中添加成员变量
    private JToggleButton allFingerprintsButton;
    private JToggleButton toggleButton;

    public FingerTab() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel topPanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        // 列数，行数
        gridBagLayout.columnWidths = new int[] { 0, 0};
        gridBagLayout.rowHeights = new int[] {5};
        // 各列占宽度比，各行占高度比
        gridBagLayout.columnWeights = new double[] { 1.0D, Double.MIN_VALUE };
        topPanel.setLayout(gridBagLayout);

        JPanel FilterPanel = new JPanel();
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.insets = new Insets(0, 5, 5, 5);
        gbc_panel_1.fill = 2;
        gbc_panel_1.gridx = 0;
        gbc_panel_1.gridy = 2;
        topPanel.add(FilterPanel, gbc_panel_1);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[] { 0, 0, 0, 0, 0 };
        gbl_panel_1.rowHeights = new int[] { 0, 0 };
        gbl_panel_1.columnWeights = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, Double.MIN_VALUE};
        gbl_panel_1.rowWeights = new double[] { 0.0D, Double.MIN_VALUE };
        FilterPanel.setLayout(gbl_panel_1);

        // 在添加 "Requests Total" 和 lbRequestCount 之前添加一个占位组件
        Component leftStrut = Box.createHorizontalStrut(5); // 你可以根据需要调整这个值
        GridBagConstraints gbc_leftStrut = new GridBagConstraints();
        gbc_leftStrut.insets = new Insets(0, 0, 0, 5);
        gbc_leftStrut.fill = GridBagConstraints.HORIZONTAL;
        gbc_leftStrut.weightx = 1.0; // 这个值决定了 leftStrut 占据的空间大小
        gbc_leftStrut.gridx = 10;
        gbc_leftStrut.gridy = 0;
        FilterPanel.add(leftStrut, gbc_leftStrut);

        // 转发url总数，默认0
        JLabel lbRequest = new JLabel("Requests Total:");
        GridBagConstraints gbc_lbRequest = new GridBagConstraints();
        gbc_lbRequest.insets = new Insets(0, 0, 0, 5);
        gbc_lbRequest.fill = GridBagConstraints.HORIZONTAL;
        gbc_lbRequest.weightx = 0.0;
        gbc_lbRequest.gridx = 0;
        gbc_lbRequest.gridy = 0;
        FilterPanel.add(lbRequest, gbc_lbRequest);

        lbRequestCount = new JLabel("0");
        lbRequestCount.setForeground(new Color(0,0,255));
        GridBagConstraints gbc_lbRequestCount = new GridBagConstraints();
        gbc_lbRequestCount.insets = new Insets(0, 0, 0, 5);
        gbc_lbRequest.fill = GridBagConstraints.HORIZONTAL;
        gbc_lbRequest.weightx = 0.0;
        gbc_lbRequestCount.gridx = 1;
        gbc_lbRequestCount.gridy = 0;
        FilterPanel.add(lbRequestCount, gbc_lbRequestCount);

        // 转发成功url数，默认0
        JLabel lbSucces = new JLabel("Finger Success:");
        GridBagConstraints gbc_lbSucces = new GridBagConstraints();
        gbc_lbSucces.insets = new Insets(0, 0, 0, 5);
        gbc_lbSucces.fill = 0;
        gbc_lbSucces.gridx = 2;
        gbc_lbSucces.gridy = 0;
        FilterPanel.add(lbSucces, gbc_lbSucces);

        lbSuccessCount = new JLabel("0");
        lbSuccessCount.setForeground(new Color(0, 255, 0));
        GridBagConstraints gbc_lbSuccessCount = new GridBagConstraints();
        gbc_lbSuccessCount.insets = new Insets(0, 0, 0, 5);
        gbc_lbSuccessCount.fill = 0;
        gbc_lbSuccessCount.gridx = 3;
        gbc_lbSuccessCount.gridy = 0;
        FilterPanel.add(lbSuccessCount, gbc_lbSuccessCount);

        // 初始化按钮
        allFingerprintsButton = new JToggleButton(getImageIcon("/icon/allButtonIcon.png", 30, 30));
        allFingerprintsButton.setSelectedIcon(getImageIcon("/icon/importantButtonIcon.png", 30, 30));
        allFingerprintsButton.setPreferredSize(new Dimension(30, 30));
        allFingerprintsButton.setBorder(null);  // 设置无边框
        allFingerprintsButton.setFocusPainted(false);  // 移除焦点边框
        allFingerprintsButton.setContentAreaFilled(false);  // 移除选中状态下的背景填充
        allFingerprintsButton.setToolTipText("指纹匹配：所有指纹");
        toggleButton = new JToggleButton(getImageIcon("/icon/openButtonIcon.png", 40, 24));
        toggleButton.setSelectedIcon(getImageIcon("/icon/shutdownButtonIcon.png", 40, 24));
        toggleButton.setPreferredSize(new Dimension(50, 24));
        toggleButton.setBorder(null);  // 设置无边框
        toggleButton.setFocusPainted(false);  // 移除焦点边框
        toggleButton.setContentAreaFilled(false);  // 移除选中状态下的背景填充
        toggleButton.setToolTipText("指纹识别功能开");

        // 添加填充以在左侧占位
        GridBagConstraints gbc_leftFiller = new GridBagConstraints();
        gbc_leftFiller.weightx = 1; // 使得这个组件吸收额外的水平空间
        gbc_leftFiller.gridx = 5; // 位置设置为第一个单元格
        gbc_leftFiller.gridy = 0; // 第一行
        gbc_leftFiller.fill = GridBagConstraints.HORIZONTAL; // 水平填充
        FilterPanel.add(Box.createHorizontalGlue(), gbc_leftFiller);

        // 设置按钮的 GridBagConstraints
        GridBagConstraints gbc_buttons = new GridBagConstraints();
        gbc_buttons.insets = new Insets(0, 5, 0, 5);
        gbc_buttons.gridx = 6; // 设置按钮的横坐标位置
        gbc_buttons.gridy = 0; // 设置按钮的纵坐标位置
        gbc_buttons.fill = GridBagConstraints.NONE; // 不填充

        // 在 FilterPanel 中添加 allFingerprintsButton
        FilterPanel.add(allFingerprintsButton, gbc_buttons);

        // 在 FilterPanel 中添加 toggleButton
        gbc_buttons.gridx = 7; // 将横坐标位置移动到下一个单元格
        FilterPanel.add(toggleButton, gbc_buttons);

        // 添加填充以在右侧占位
        GridBagConstraints gbc_rightFiller = new GridBagConstraints();
        gbc_rightFiller.weightx = 1; // 使得这个组件吸收额外的水平空间
        gbc_rightFiller.gridx = 8; // 位置设置为最后一个单元格
        gbc_rightFiller.gridy = 0; // 第一行
        gbc_rightFiller.fill = GridBagConstraints.HORIZONTAL; // 水平填充
        FilterPanel.add(Box.createHorizontalGlue(), gbc_rightFiller);

        // 在FingerTab类中添加事件监听器
        allFingerprintsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 检查按钮状态并切换
                if (allFingerprintsButton.isSelected()) {
                    // 按钮选中状态，应用过滤器以仅显示 isImportant 列为 true 的行
                    RowFilter<TableModel, Integer> importanceFilter = new RowFilter<TableModel, Integer>() {
                        @Override
                        public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                            // 假设 isImportant 是第7列（注意列索引从0开始计数）
                            Boolean isImportant = (Boolean) entry.getValue(7); // 根据您的表格实际的列索引来调整
                            return isImportant != null && isImportant;
                        }
                    };
                    ((TableRowSorter<TableModel>) logTable.getRowSorter()).setRowFilter(importanceFilter);
                } else {
                    // 按钮未选中状态，移除过滤器以显示所有行
                    ((TableRowSorter<TableModel>) logTable.getRowSorter()).setRowFilter(null);
                }
                // 更新FingerConfigTab中的按钮状态
                FingerConfigTab.allFingerprintsButton.setSelected(allFingerprintsButton.isSelected());
                // 调用FingerConfigTab中的toggleFingerprintsDisplay方法
                FingerConfigTab.toggleFingerprintsDisplay(allFingerprintsButton.isSelected());
            }
        });

        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 例如，更新FingerConfigTab中的按钮状态
                BurpExtender.tags.fingerConfigTab.toggleButton.setSelected(toggleButton.isSelected());
            }
        });


        // 添加一个 "清除" 按钮
        JButton btnClear = new JButton("清除");
        GridBagConstraints gbc_btnClear = new GridBagConstraints();
        gbc_btnClear.insets = new Insets(0, 0, 0, 5);
        gbc_btnClear.fill = 0;
        gbc_btnClear.gridx = 11;  // 根据该值来确定是确定从左到右的顺序
        gbc_btnClear.gridy = 0;
        FilterPanel.add(btnClear, gbc_btnClear);

        // 功能按钮
        JPopupMenu moreMenu = new JPopupMenu("功能");
        JMenuItem exportItem = new JMenuItem("导出");
        moreMenu.add(exportItem);
        exportItem.setIcon(getImageIcon("/icon/exportItem.png", 17, 17));
        moreMenu.add(exportItem);
        JButton moreButton = new JButton();
        moreButton.setIcon(getImageIcon("/icon/moreButton.png", 17, 17));
        GridBagConstraints gbc_btnMore = new GridBagConstraints();
        gbc_btnClear.insets = new Insets(0, 0, 0, 5);
        gbc_btnClear.fill = 0;
        gbc_btnClear.gridx = 12;  // 根据该值来确定是确定从左到右的顺序
        gbc_btnClear.gridy = 0;
        FilterPanel.add(moreButton, gbc_btnMore);

        exportItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Specify a file to save");

                // 设置默认文件名
                fileChooser.setSelectedFile(new File("ExportedData.xlsx"));

                // 限制文件类型为.xlsx
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xlsx");
                fileChooser.setFileFilter(filter);

                int userSelection = fileChooser.showSaveDialog(contentPane);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    // 确保文件有正确的扩展名
                    if (!fileToSave.toString().toLowerCase().endsWith(".xlsx")) {
                        fileToSave = new File(fileToSave.toString() + ".xlsx");
                    }
                    // 导出表格数据到Excel文件
                    exportTableToExcel(fileToSave);

                }
            }
        });


        // 点击”功能“的监听事件
        moreButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                moreMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        contentPane.add(topPanel,BorderLayout.NORTH);

        tagsPanel = new JPanel();
        tagsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        GridBagConstraints gbc_tagsPanel = new GridBagConstraints();
        gbc_tagsPanel.insets = new Insets(0, 0, 5, 0);
        gbc_tagsPanel.fill = GridBagConstraints.HORIZONTAL;
        gbc_tagsPanel.gridx = 0;
        gbc_tagsPanel.gridy = 1;  // 新的行
        topPanel.add(tagsPanel, gbc_tagsPanel);

        JLabel allLabel = new JLabel("全部");
        allLabel.setOpaque(true);  // 设置为不透明
        allLabel.setBackground(new Color(200, 200, 200));  // 设置背景颜色为浅灰色
        allLabel.setForeground(Color.BLACK);  // 设置字体颜色为黑色

        // 为标签添加一个有颜色的边框，边框内有5像素的填充
        allLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),  // 外部边框，颜色为深灰色，宽度为2像素
                BorderFactory.createEmptyBorder(5, 5, 5, 5)  // 内部填充，宽度为5像素
        ));
        allLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 当用户点击 "全部"，展示所有的数据
                ((TableRowSorter<TableModel>)logTable.getRowSorter()).setRowFilter(null);
            }
        });
        tagsPanel.add(allLabel);

        contentPane.add(topPanel,BorderLayout.NORTH);  // 只在 contentPane 的北部添加一个组件

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(0.5);
        contentPane.add(splitPane, BorderLayout.CENTER);

        HttpLogTableModel model = new HttpLogTableModel();
        logTable = new HttpLogTable(model);
        // 创建居中渲染器实例
        CenterTableCellRenderer centerRenderer = new CenterTableCellRenderer();
        // 创建自定义的单元格渲染器实例
        IconTableCellRenderer iconRenderer = new IconTableCellRenderer();
        logTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        logTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        logTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        logTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        logTable.getColumnModel().getColumn(7).setCellRenderer(iconRenderer);
        logTable.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
        logTable.setAutoCreateRowSorter(true);  // 添加这一行来启用自动创建行排序器
        logTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        logTable.setRowSelectionAllowed(true);

        // 在FingerConfigTab构造函数中设置表头渲染器和监听器的代码
        JTableHeader header = logTable.getTableHeader();
        TableColumnModel columnModel = header.getColumnModel();
        TableColumn typeColumn = columnModel.getColumn(6); // 假定类型列的索引是1

        // 设置表头渲染器
        typeColumn.setHeaderRenderer(new HeaderIconRenderer());
        // 在您的FingerConfigTab构造函数中
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (logTable.getColumnModel().getColumnIndexAtX(e.getX()) == 6) { // 假设类型列的索引是1
                    showFilterPopup(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                HashMap<String, Integer> resultCounts = new HashMap<>();
                // 遍历表格中所有行
                for(int i = 0; i < model.getRowCount(); i++) {
                    String result = (String) model.getValueAt(i, 5); // 获取结果值
                    String[] parts = result.split(", "); // 根据", "进行切分
                    for(String part : parts) {
                        resultCounts.put(part, resultCounts.getOrDefault(part, 0) + 1); // 添加到映射中进行去重，并计数
                    }
                }
                clearAllResultLabels();

                // 创建一个 TreeMap 并进行反向排序
                TreeMap<Integer, LinkedList<String>> sortedResults = new TreeMap<>(Collections.reverseOrder());
                for(Map.Entry<String, Integer> entry : resultCounts.entrySet()) {
                    sortedResults.computeIfAbsent(entry.getValue(), k -> new LinkedList<>()).add(entry.getKey());
                }

                // 添加新的结果标签
                for(Map.Entry<Integer, LinkedList<String>> entry : sortedResults.entrySet()) {
                    Integer count = entry.getKey();
                    for(String result : entry.getValue()) {
                        FingerTab.addNewResultLabel(result + " (" + count + ")", model);
                    }
                }
            }
        });


        // 创建右键菜单
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem clearItem = new JMenuItem("清除");
        popupMenu.add(clearItem);
        // 将右键菜单添加到表格
        logTable.setComponentPopupMenu(popupMenu);

        // 为菜单项添加行为
        clearItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int successRequestsCount = Integer.parseInt(lbSuccessCount.getText()) - logTable.getSelectedRows().length;
                lbSuccessCount.setText(Integer.toString(successRequestsCount));
                model.removeSelectedRows(logTable);
            }
        });

        JScrollPane jspLogTable = new JScrollPane(logTable);
        splitPane.setTopComponent(jspLogTable);

        // 添加点击事件监听器
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 清除表格数据
                lbRequestCount.setText("0");
                lbSuccessCount.setText("0");
                model.setRowCount();
                BurpExtender.hasScanDomainSet = new HashSet<>();
                clearAllResultLabels();
            }
        });


        JTabbedPane tabs = new JTabbedPane();
        requestViewer = BurpExtender.callbacks.createMessageEditor(this, false);
        responseViewer = BurpExtender.callbacks.createMessageEditor(this, false);
        resultDeViewer = BurpExtender.callbacks.createTextEditor();

        tabs.addTab("Result Details", resultDeViewer.getComponent());
        tabs.addTab("Request", requestViewer.getComponent());
        tabs.addTab("Original Response", responseViewer.getComponent());
        splitPane.setBottomComponent(tabs);

        BurpExtender.callbacks.customizeUiComponent(topPanel);
    }

    public Component getComponet(){
        return contentPane;
    }

    public IHttpService getHttpService() {
        return currentlyDisplayedItem.getHttpService();
    }

    public byte[] getRequest() {
        return currentlyDisplayedItem.getRequest();
    }

    public byte[] getResponse() {
        return currentlyDisplayedItem.getResponse();
    }

    public static void addNewResultLabel(String result, HttpLogTableModel model) {
        // 创建新的标签
        JLabel newLabel = new JLabel(result);
        newLabel.setOpaque(true);  // 设置为不透明
        newLabel.setBackground(new Color(200, 200, 200));  // 设置背景颜色为浅灰色
        newLabel.setForeground(Color.BLACK);  // 设置字体颜色为黑色

        // 为标签添加一个有颜色的边框，边框内有5像素的填充
        newLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),  // 外部边框，颜色为深灰色，宽度为2像素
                BorderFactory.createEmptyBorder(5, 5, 5, 5)  // 内部填充，宽度为5像素
        ));

        newLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 当用户点击某个标签时，展示所有包含该标签文本的结果
                String filterWithoutCount = result.replaceAll("\\(.*\\)", "").trim();
                ((TableRowSorter<TableModel>)logTable.getRowSorter())
                        .setRowFilter(RowFilter.regexFilter(filterWithoutCount, 5));
            }
        });

        // 添加新的标签到面板和 resultMap
        tagsPanel.add(newLabel);
        resultMap.put(result, newLabel);
        // 重新验证和重绘面板
        tagsPanel.revalidate();
        tagsPanel.repaint();
    }

    public void clearAllResultLabels() {
        for (Component component : tagsPanel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                // 如果标签的文本不是"全部"，则移除
                if (!"全部".equals(label.getText())) {
                    tagsPanel.remove(component);
                }
            }
        }
        tagsPanel.revalidate();
        tagsPanel.repaint();
    }


    public ImageIcon getImageIcon(String iconPath, int xWidth, int yWidth){
        // 根据按钮的大小缩放图标
        URL iconURL = getClass().getResource(iconPath);
        ImageIcon originalIcon = new ImageIcon(iconURL);
        Image img = originalIcon.getImage();
        Image newImg = img.getScaledInstance(xWidth, yWidth, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

    public class CenterTableCellRenderer extends DefaultTableCellRenderer {
        public CenterTableCellRenderer() {
            setHorizontalAlignment(JLabel.CENTER); // 设置水平对齐为居中
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            return this;
        }
    }

    public class IconTableCellRenderer extends DefaultTableCellRenderer {

        public IconTableCellRenderer() {
            setHorizontalAlignment(CENTER); // 设置居中
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // 设置文本为空，因为我们只显示图标
            setText("");

            // 根据单元格值设置相应图标
            if (value instanceof Boolean) {
                if ((Boolean) value) {
                    setIcon(getImageIcon("/icon/importantButtonIcon.png", 15, 15));
                } else {
                    setIcon(getImageIcon("/icon/normalIcon.png", 15, 15));
                }
            } else {
                setIcon(null); // 如果值不是布尔类型，则不显示图标
            }

            return this;
        }
    }

    class HeaderIconRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // 保留原始行为
            Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // 如果是类型列
            if (column == 6) {
                setIcon(getImageIcon("/icon/filterIcon.png", 17, 17));
                setHorizontalAlignment(JLabel.CENTER);
                setHorizontalTextPosition(JLabel.LEFT);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setIcon(null);
            }
            return comp;
        }
    }

    private void showFilterPopup(Component invoker, int x, int y) {
        JPopupMenu filterMenu = new JPopupMenu();

        // “全部”选项用于移除过滤
        JMenuItem allItem = new JMenuItem("全部");
        allItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTableByType(null); // 移除过滤，显示全部
            }
        });
        filterMenu.add(allItem);

        filterMenu.add(new JSeparator()); // 分隔线

        // 为每个独特的类型创建菜单项
        for (String type : BurpExtender.tags.fingerConfigTab.uniqueTypes) {
            JMenuItem menuItem = new JMenuItem(type);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    filterTableByType(type); // 根据选中的类型过滤表格
                }
            });
            filterMenu.add(menuItem);
        }

        filterMenu.show(invoker, x, y); // 显示菜单
    }

    private void filterTableByType(String type) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>((TableModel) logTable.getModel());
        logTable.setRowSorter(sorter);

        if (type == null || type.equals("全部")) {
            // 显示所有行
            sorter.setRowFilter(null);
        } else {
            // 只显示type列符合条件的行
            sorter.setRowFilter(RowFilter.regexFilter(type, 6)); // 假设类型列的索引是1
        }
    }
    public void exportTableToExcel(File file) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Table Data");
        TableModel model = logTable.getModel();

        // 创建表头
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < model.getColumnCount(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(model.getColumnName(i));
        }

        // 填充数据
        for (int j = 0; j < model.getRowCount(); j++) {
            Row row = sheet.createRow(j + 1);
            for (int k = 0; k < model.getColumnCount(); k++) {
                Cell cell = row.createCell(k);
                Object value = model.getValueAt(j, k);
                if (value instanceof Boolean) {
                    cell.setCellValue((Boolean) value);
                } else if (value instanceof Number) {
                    cell.setCellValue(((Number) value).doubleValue());
                } else {
                    cell.setCellValue(value.toString());
                }
            }
        }

        // 自动调整所有列的宽度
        for (int i = 0; i < model.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);
        }

        // 写入到文件
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
            JOptionPane.showMessageDialog(contentPane, "导出成功！保存路径为：\n" + file.getAbsolutePath(), "导出成功", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(contentPane, "导出失败，原因：\n" + e.getMessage(), "导出失败", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

