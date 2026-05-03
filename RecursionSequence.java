import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class RecursionSequence extends JFrame {

    private static final Color BG      = new Color(10, 10, 10);
    private static final Color SURFACE = new Color(16, 16, 16);
    private static final Color BORDER  = new Color(30, 30, 30);
    private static final Color DIM     = new Color(60, 60, 60);
    private static final Color MUTED   = new Color(100, 100, 100);
    private static final Color SUBTLE  = new Color(160, 160, 160);
    private static final Color TEXT    = new Color(230, 230, 230);
    private static final Color ACCENT  = new Color(220, 180, 100);
    private static final Color ERR     = new Color(180, 70, 70);

    private static final Font DISPLAY = new Font("Georgia", Font.PLAIN, 20);
    private static final Font BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font MONO    = new Font("Consolas", Font.PLAIN, 13);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new RecursionSequence().setVisible(true);
        });
    }

    public RecursionSequence() {
        setTitle("Recursion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(740, 580);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.setBorder(BorderFactory.createEmptyBorder(32, 36, 24, 36));
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel();
        p.setBackground(BG);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Sequence Calculator");
        title.setFont(DISPLAY);
        title.setForeground(TEXT);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Fibonacci  ·  Lucas  ·  Tribonacci");
        sub.setFont(SMALL);
        sub.setForeground(MUTED);
        sub.setAlignmentX(LEFT_ALIGNMENT);

        p.add(title);
        p.add(Box.createVerticalStrut(5));
        p.add(sub);
        p.add(Box.createVerticalStrut(20));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(BORDER);
        p.add(sep);
        p.add(Box.createVerticalStrut(20));

        return p;
    }

    private JPanel buildCenter() {
        JPanel cards = new JPanel(new CardLayout());
        cards.setBackground(BG);

        JTextArea output = new JTextArea(7, 40);
        output.setFont(MONO);
        output.setBackground(SURFACE);
        output.setForeground(ACCENT);
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        output.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        output.setCaretColor(ACCENT);

        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.setBackground(SURFACE);
        scroll.getViewport().setBackground(SURFACE);

        JPanel menuCard = buildMenuCard(cards, output);
        cards.add(menuCard, "menu");

        String[] names    = {"Fibonacci", "Lucas", "Tribonacci"};
        int[]    minimums = {3, 3, 4};
        String[] hints    = {
            "Starts with 0, 1 — each term is the sum of the two before it.",
            "Starts with 2, 1 — same recurrence as Fibonacci.",
            "Starts with 0, 0, 1 — each term is the sum of the three before it."
        };

        for (int i = 0; i < 3; i++) {
            cards.add(buildSeqCard(names[i], hints[i], minimums[i], output, cards, i), "seq" + i);
        }

        ((CardLayout) cards.getLayout()).show(cards, "menu");

        JPanel wrapper = new JPanel(new BorderLayout(0, 14));
        wrapper.setBackground(BG);
        wrapper.add(cards, BorderLayout.CENTER);
        wrapper.add(scroll, BorderLayout.SOUTH);

        return wrapper;
    }

    private JPanel buildMenuCard(JPanel cards, JTextArea output) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.fill  = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("Choose a sequence");
        label.setFont(SMALL);
        label.setForeground(MUTED);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        gc.gridy  = 0;
        gc.insets = new Insets(0, 0, 16, 0);
        p.add(label, gc);

        String[] opts  = {"Fibonacci Numbers", "Lucas Numbers", "Tribonacci Numbers", "Exit"};
        boolean[] exit = {false, false, false, true};

        for (int i = 0; i < opts.length; i++) {
            final int idx    = i;
            final boolean ex = exit[i];
            JButton btn = makeMenuBtn(opts[i], ex);
            btn.addActionListener(e -> {
                if (ex) {
                    int c = JOptionPane.showConfirmDialog(this,
                        "Exit the program?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (c == JOptionPane.YES_OPTION) System.exit(0);
                } else {
                    output.setText("");
                    ((CardLayout) cards.getLayout()).show(cards, "seq" + idx);
                }
            });
            gc.gridy  = i + 1;
            gc.insets = new Insets(4, 0, 4, 0);
            p.add(btn, gc);
        }

        return p;
    }

    private JPanel buildSeqCard(String name, String hint, int min,
                                JTextArea output, JPanel cards, int idx) {
        JPanel card = new JPanel();
        card.setBackground(BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(name + " Numbers");
        nameLabel.setFont(new Font("Georgia", Font.PLAIN, 15));
        nameLabel.setForeground(TEXT);
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel hintLabel = new JLabel(hint);
        hintLabel.setFont(SMALL);
        hintLabel.setForeground(MUTED);
        hintLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel validLabel = new JLabel("Valid input:  n > " + (min - 1));
        validLabel.setFont(SMALL);
        validLabel.setForeground(DIM);
        validLabel.setAlignmentX(LEFT_ALIGNMENT);

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(hintLabel);
        card.add(Box.createVerticalStrut(2));
        card.add(validLabel);
        card.add(Box.createVerticalStrut(16));

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        inputRow.setBackground(BG);
        inputRow.setAlignmentX(LEFT_ALIGNMENT);

        JLabel inputLabel = new JLabel("Terms");
        inputLabel.setFont(SMALL);
        inputLabel.setForeground(SUBTLE);
        inputRow.add(inputLabel);

        JTextField field = styledField(6);
        inputRow.add(field);

        JButton run  = makeBtn("Run",    false);
        JButton back = makeBtn("← Back", true);
        inputRow.add(run);
        inputRow.add(back);

        card.add(inputRow);
        card.add(Box.createVerticalStrut(14));

        JLabel invLabel = new JLabel("Invalid input / output  —  values  ≤  " + (min - 1));
        invLabel.setFont(SMALL);
        invLabel.setForeground(ERR);
        invLabel.setAlignmentX(LEFT_ALIGNMENT);
        card.add(invLabel);
        card.add(Box.createVerticalStrut(6));

        JTable table = buildInvalidTable(min);
        JScrollPane ts = new JScrollPane(table);
        ts.setPreferredSize(new Dimension(660, 88));
        ts.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));
        ts.setAlignmentX(LEFT_ALIGNMENT);
        ts.setBorder(BorderFactory.createLineBorder(BORDER));
        ts.getViewport().setBackground(SURFACE);
        card.add(ts);

        run.addActionListener(e -> {
            String raw = field.getText().trim();
            if (!raw.matches("-?\\d+")) {
                output.setForeground(ERR);
                output.setText("Invalid — \"" + raw + "\" is not an integer.\n"
                    + "Enter a whole number greater than " + (min - 1) + ".");
                return;
            }
            int n = Integer.parseInt(raw);
            if (n < min) {
                String why = n <= 0 ? "must be positive" : "below minimum (" + min + " or more required)";
                output.setForeground(ERR);
                output.setText("Invalid input: " + n + " — " + why + ".\n"
                    + "Valid range for " + name + ":  n > " + (min - 1) + ".");
                return;
            }
            long[] seq = idx == 0 ? fibonacci(n) : idx == 1 ? lucas(n) : tribonacci(n);
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(" Numbers\n");
            sb.append("n = ").append(n).append("\n\n");
            for (int k = 0; k < seq.length; k++) {
                sb.append(seq[k]);
                if (k < seq.length - 1) sb.append(k % 10 == 9 ? ",\n" : ", ");
            }
            output.setForeground(ACCENT);
            output.setText(sb.toString());
        });

        back.addActionListener(e -> {
            output.setText("");
            field.setText("");
            ((CardLayout) cards.getLayout()).show(cards, "menu");
        });

        return card;
    }

    private long[] fibonacci(int n) {
        long[] a = new long[n];
        a[0] = 0;
        if (n > 1) a[1] = 1;
        for (int i = 2; i < n; i++) a[i] = a[i-1] + a[i-2];
        return a;
    }

    private long[] lucas(int n) {
        long[] a = new long[n];
        a[0] = 2;
        if (n > 1) a[1] = 1;
        for (int i = 2; i < n; i++) a[i] = a[i-1] + a[i-2];
        return a;
    }

    private long[] tribonacci(int n) {
        long[] a = new long[n];
        a[0] = 0;
        if (n > 1) a[1] = 0;
        if (n > 2) a[2] = 1;
        for (int i = 3; i < n; i++) a[i] = a[i-1] + a[i-2] + a[i-3];
        return a;
    }

    private JTable buildInvalidTable(int min) {
        String[] cols = {"Value", "Status"};
        List<Object[]> rows = new ArrayList<>();
        for (int v = -min; v <= 0; v++) {
            rows.add(new Object[]{v, v < 0 ? "Negative — Invalid Input/Output" : "Zero — Invalid Input/Output"});
        }
        for (int v = 1; v < min; v++) {
            rows.add(new Object[]{v, "Too few terms — Invalid Input/Output"});
        }

        JTable t = new JTable(rows.toArray(new Object[0][]), cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        t.setBackground(SURFACE);
        t.setForeground(SUBTLE);
        t.setFont(MONO);
        t.setRowHeight(20);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));

        DefaultTableCellRenderer cr = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                c.setBackground(row % 2 == 0 ? SURFACE : new Color(20, 20, 20));
                c.setForeground(col == 0 ? ERR : MUTED);
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return c;
            }
        };
        t.setDefaultRenderer(Object.class, cr);

        JTableHeader header = t.getTableHeader();
        header.setBackground(new Color(14, 14, 14));
        header.setForeground(MUTED);
        header.setFont(SMALL);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
            .setHorizontalAlignment(SwingConstants.LEFT);

        t.getColumnModel().getColumn(0).setPreferredWidth(70);
        t.getColumnModel().getColumn(1).setPreferredWidth(590);

        return t;
    }

    private JButton makeMenuBtn(String text, boolean danger) {
        JButton btn = new JButton(text);
        btn.setFont(BODY);
        btn.setForeground(danger ? ERR : TEXT);
        btn.setBackground(SURFACE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(danger ? new Color(60, 25, 25) : BORDER),
            BorderFactory.createEmptyBorder(10, 0, 10, 0)));
        btn.setPreferredSize(new Dimension(280, 42));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Color hover = danger ? new Color(30, 14, 14) : new Color(22, 22, 22);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover);   }
            public void mouseExited(MouseEvent e)  { btn.setBackground(SURFACE); }
        });
        return btn;
    }

    private JButton makeBtn(String text, boolean ghost) {
        JButton btn = new JButton(text);
        btn.setFont(SMALL);
        btn.setForeground(ghost ? MUTED : BG);
        btn.setBackground(ghost ? SURFACE : ACCENT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ghost ? BORDER : ACCENT),
            BorderFactory.createEmptyBorder(5, 14, 5, 14)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Color hover = ghost ? new Color(22, 22, 22) : ACCENT.brighter();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover);                       }
            public void mouseExited(MouseEvent e)  { btn.setBackground(ghost ? SURFACE : ACCENT);    }
        });
        return btn;
    }

    private JTextField styledField(int cols) {
        JTextField f = new JTextField(cols);
        f.setFont(MONO);
        f.setBackground(SURFACE);
        f.setForeground(TEXT);
        f.setCaretColor(ACCENT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        return f;
    }
}