import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class RecursionSequence extends JFrame {

    private static final Color BG      = new Color(9,  9,  9);
    private static final Color SURFACE = new Color(15, 15, 15);
    private static final Color CARD    = new Color(18, 18, 18);
    private static final Color BORDER  = new Color(32, 32, 32);
    private static final Color DIM     = new Color(48, 48, 48);
    private static final Color MUTED   = new Color(90, 90, 90);
    private static final Color SUBTLE  = new Color(145, 145, 145);
    private static final Color TEXT    = new Color(225, 225, 225);
    private static final Color ACCENT  = new Color(212, 170, 84);
    private static final Color ACCENT2 = new Color(170, 130, 55);
    private static final Color ERR     = new Color(185, 75, 75);
    private static final Color ACCEPT  = new Color(65, 145, 95);
    private static final Font DISPLAY = new Font("Georgia",   Font.PLAIN,  21);
    private static final Font BODY    = new Font("Segoe UI",  Font.PLAIN,  13);
    private static final Font SMALL   = new Font("Segoe UI",  Font.PLAIN,  11);
    private static final Font MONO    = new Font("Consolas",  Font.PLAIN,  13);
    private static final Font TINY    = new Font("Segoe UI",  Font.PLAIN,  10);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new RecursionSequence().setVisible(true);
        });
    }

    public RecursionSequence() {
        setTitle("Recursion — Sequence Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 740);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.setBorder(BorderFactory.createEmptyBorder(28, 38, 22, 38));
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

        JPanel tagRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        tagRow.setBackground(BG);
        tagRow.setAlignmentX(LEFT_ALIGNMENT);
        for (String tag : new String[]{"Fibonacci", "Lucas", "Tribonacci"}) {
            tagRow.add(makeTag(tag));
        }

        p.add(title);
        p.add(Box.createVerticalStrut(7));
        p.add(tagRow);
        p.add(Box.createVerticalStrut(20));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(BORDER);
        sep.setAlignmentX(LEFT_ALIGNMENT);
        p.add(sep);
        p.add(Box.createVerticalStrut(20));
        return p;
    }

    private JLabel makeTag(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lbl.setForeground(MUTED);
        lbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DIM),
            BorderFactory.createEmptyBorder(2, 7, 2, 7)));
        return lbl;
    }

    private JPanel buildCenter() {
        JPanel cards = new JPanel(new CardLayout());
        cards.setBackground(BG);

        JTextArea output = new JTextArea(4, 40);
        output.setFont(MONO);
        output.setBackground(SURFACE);
        output.setForeground(ACCENT);
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        output.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        output.setCaretColor(ACCENT);

        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        scroll.setBackground(SURFACE);
        scroll.getViewport().setBackground(SURFACE);
        scroll.setPreferredSize(new Dimension(0, 110));

        cards.add(buildMenuCard(cards, output), "menu");

        String[] names    = {"Fibonacci", "Lucas", "Tribonacci"};
        int[]    minimums = {3, 3, 4};
        String[] hints    = {
            "f(n) = f(n-1) + f(n-2),  f(0)=0, f(1)=",
            "L(n) = L(n-1) + L(n-2),  L(0)=2, L(1)=1",
            "T(n) = T(n-1) + T(n-2) + T(n-3),  T(0)=0, T(1)=0, T(2)=1"
        };

        for (int i = 0; i < 3; i++) {
            cards.add(buildSeqCard(names[i], hints[i], minimums[i], output, cards, i), "seq" + i);
        }

        ((CardLayout) cards.getLayout()).show(cards, "menu");

        JPanel wrapper = new JPanel(new BorderLayout(0, 14));
        wrapper.setBackground(BG);
        wrapper.add(cards, BorderLayout.CENTER);
        wrapper.add(buildOutputPanel(output, scroll), BorderLayout.SOUTH);
        return wrapper;
    }

    private JPanel buildOutputPanel(JTextArea output, JScrollPane scroll) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setBackground(BG);

        JLabel lbl = new JLabel("OUTPUT");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        lbl.setForeground(DIM);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));

        p.add(lbl, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildMenuCard(JPanel cards, JTextArea output) {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG);

        JPanel inner = new JPanel();
        inner.setBackground(BG);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Select a sequence type");
        label.setFont(SMALL);
        label.setForeground(MUTED);
        label.setAlignmentX(LEFT_ALIGNMENT);
        inner.add(label);
        inner.add(Box.createVerticalStrut(14));

        String[] opts  = {"Fibonacci Numbers", "Lucas Numbers", "Tribonacci Numbers"};
        String[] descs = {
            " ",
            " ",
            " "
        };

        for (int i = 0; i < opts.length; i++) {
            final int idx = i;
            JPanel row = makeMenuRow(opts[i], descs[i], false);
            row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            row.addMouseListener(new MouseAdapter() {
                Color normal = SURFACE;
                public void mouseEntered(MouseEvent e) { row.setBackground(new Color(22,22,22)); }
                public void mouseExited(MouseEvent e)  { row.setBackground(normal); }
                public void mouseClicked(MouseEvent e) {
                    output.setText("");
                    output.setForeground(ACCENT);
                    ((CardLayout) cards.getLayout()).show(cards, "seq" + idx);
                }
            });
            inner.add(row);
            inner.add(Box.createVerticalStrut(6));
        }

        inner.add(Box.createVerticalStrut(4));
        JPanel exitRow = makeMenuRow("Exit", "", true);
        exitRow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitRow.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { exitRow.setBackground(new Color(25,15,15)); }
            public void mouseExited(MouseEvent e)  { exitRow.setBackground(SURFACE); }
            public void mouseClicked(MouseEvent e) {
                int c = JOptionPane.showConfirmDialog(RecursionSequence.this,
                    "Exit the program?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (c == JOptionPane.YES_OPTION) System.exit(0);
            }
        });
        inner.add(exitRow);

        outer.add(inner, new GridBagConstraints());
        return outer;
    }

    private JPanel makeMenuRow(String title, String desc, boolean danger) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setBackground(SURFACE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(danger ? new Color(50,22,22) : BORDER),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        p.setMaximumSize(new Dimension(440, 60));
        p.setPreferredSize(new Dimension(440, 52));

        JLabel t = new JLabel(title);
        t.setFont(BODY);
        t.setForeground(danger ? ERR : TEXT);

        JLabel d = new JLabel(desc);
        d.setFont(TINY);
        d.setForeground(danger ? new Color(120,55,55) : MUTED);

        JPanel left = new JPanel();
        left.setBackground(null);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(t);
        left.add(Box.createVerticalStrut(2));
        left.add(d);

        JLabel arrow = new JLabel(danger ? "×" : "→");
        arrow.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        arrow.setForeground(danger ? new Color(100,45,45) : DIM);

        p.add(left, BorderLayout.CENTER);
        p.add(arrow, BorderLayout.EAST);
        return p;
    }

    private JPanel buildSeqCard(String name, String hint, int min,
                                JTextArea output, JPanel cards, int idx) {
        JPanel card = new JPanel();
        card.setBackground(BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setBackground(BG);
        titleRow.setAlignmentX(LEFT_ALIGNMENT);
        titleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel nameLabel = new JLabel(name + " Numbers");
        nameLabel.setFont(new Font("Georgia", Font.PLAIN, 16));
        nameLabel.setForeground(TEXT);

        JButton back = makeBtn("← Menu", true);
        back.addActionListener(e -> {
            output.setText("");
            output.setForeground(ACCENT);
            ((CardLayout) cards.getLayout()).show(cards, "menu");
        });

        titleRow.add(nameLabel, BorderLayout.WEST);
        titleRow.add(back, BorderLayout.EAST);
        card.add(titleRow);
        card.add(Box.createVerticalStrut(4));

        JLabel hintLabel = new JLabel(hint);
        hintLabel.setFont(SMALL);
        hintLabel.setForeground(MUTED);
        hintLabel.setAlignmentX(LEFT_ALIGNMENT);
        card.add(hintLabel);
        card.add(Box.createVerticalStrut(14));

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        inputRow.setBackground(BG);
        inputRow.setAlignmentX(LEFT_ALIGNMENT);

        JLabel inputLabel = new JLabel("n =");
        inputLabel.setFont(new Font("Georgia", Font.PLAIN, 13));
        inputLabel.setForeground(SUBTLE);
        inputRow.add(inputLabel);

        JTextField field = styledField(8);
        inputRow.add(field);

        JButton run = makeBtn("Compute", false);
        inputRow.add(Box.createHorizontalStrut(2));
        inputRow.add(run);
        card.add(inputRow);
        card.add(Box.createVerticalStrut(16));

        card.add(makeDivider("INVALID INPUT REFERENCE   n ≤ " + (min - 1)));
        card.add(Box.createVerticalStrut(8));

        JTable table = buildInvalidTable(min);
        JScrollPane ts = new JScrollPane(table);
        ts.setPreferredSize(new Dimension(680, 72));
        ts.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        ts.setAlignmentX(LEFT_ALIGNMENT);
        ts.setBorder(BorderFactory.createLineBorder(BORDER));
        ts.getViewport().setBackground(SURFACE);
        card.add(ts);
        card.add(Box.createVerticalStrut(16));

        card.add(makeDivider("STATE DIAGRAM"));
        card.add(Box.createVerticalStrut(10));

        StateDiagramPanel diagram = new StateDiagramPanel(idx);
        diagram.setAlignmentX(LEFT_ALIGNMENT);
        diagram.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        card.add(diagram);

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
                String why = n <= 0 ? "must be positive"
                           : "minimum is " + min + " for " + name;
                output.setForeground(ERR);
                output.setText("Invalid input: " + n + " — " + why + ".\n"
                    + "Valid range:  n ≥ " + min + ".");
                return;
            }
            long[] seq = idx == 0 ? fibonacci(n) : idx == 1 ? lucas(n) : tribonacci(n);
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(" (n=").append(n).append(")\n\n");
            for (int k = 0; k < seq.length; k++) {
                sb.append(seq[k]);
                if (k < seq.length - 1) sb.append(k % 10 == 9 ? ",\n" : ",  ");
            }
            output.setForeground(ACCENT);
            output.setText(sb.toString());
            output.setCaretPosition(0);
        });

        field.addActionListener(e -> run.doClick());

        return card;
    }

    private JPanel makeDivider(String label) {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setBackground(BG);
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        lbl.setForeground(DIM);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);

        p.add(lbl, BorderLayout.WEST);
        p.add(sep, BorderLayout.CENTER);
        return p;
    }

    static class StateDiagramPanel extends JPanel {

        private final int type;
        private static final Color DIAG_BG      = new Color(12, 12, 12);
        private static final Color NODE_FILL     = new Color(22, 22, 22);
        private static final Color NODE_STROKE   = new Color(58, 58, 58);
        private static final Color START_STROKE  = new Color(212, 170, 84);
        private static final Color ACCEPT_STROKE = new Color(65, 145, 95);
        private static final Color REJECT_STROKE = new Color(175, 65, 65);
        private static final Color REJECT_FILL   = new Color(28, 14, 14);
        private static final Color ARROW_COL     = new Color(72, 72, 72);
        private static final Color START_ARROW   = new Color(212, 170, 84, 180);
        private static final Color LABEL_COL     = new Color(120, 120, 120);
        private static final Color STATE_TEXT    = new Color(210, 210, 210);

        StateDiagramPanel(int type) {
            this.type = type;
            setBackground(DIAG_BG);
            setBorder(BorderFactory.createLineBorder(new Color(26, 26, 26)));
            setPreferredSize(new Dimension(680, 170));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,    RenderingHints.VALUE_STROKE_PURE);

            if (type == 0 || type == 1) drawFibLucas(g2);
            else                         drawTribonacci(g2);
        }

        private void drawFibLucas(Graphics2D g2) {
            int w  = getWidth();
            int h  = getHeight();
            int r  = 24;         
            int cy = 62;         
            int ry = 138;        
            int pad    = 60;
            int gap    = (w - pad * 2 - r * 2 * 4) / 3;
            int step   = r * 2 + gap;

            int x0 = pad + r;
            int x1 = x0 + step;
            int x2 = x1 + step;
            int x3 = x2 + step;
            int rx = (x1 + x2) / 2;   

            String seed0 = type == 0 ? "read 0" : "read 2";
            String seed1 = "read 1";

            drawStartArrow(g2, x0 - r - 34, cy, x0 - r - 2, cy);

            drawNode(g2, x0, cy, r, "q₀", NODE_STROKE, false);
            drawNode(g2, x1, cy, r, "q₁", NODE_STROKE, false);
            drawNode(g2, x2, cy, r, "q₂", NODE_STROKE, false);
            drawNode(g2, x3, cy, r, "q₃", ACCEPT_STROKE, true);
            drawRejectNode(g2, rx, ry, r);
            drawArrow(g2, x0 + r + 2, cy, x1 - r - 2, cy);
            drawArrow(g2, x1 + r + 2, cy, x2 - r - 2, cy);
            drawArrow(g2, x2 + r + 2, cy, x3 - r - 2, cy);
            drawSelfLoop(g2, x3, cy, r, "loop");
            drawLabelAbove(g2, (x0 + x1) / 2, cy - r - 4, seed0);
            drawLabelAbove(g2, (x1 + x2) / 2, cy - r - 4, seed1);
            drawLabelAbove(g2, (x2 + x3) / 2, cy - r - 4, "T[n]=prev₁+prev₂");
            drawCurvedArrowDown(g2, x0, cy + r, rx, ry - r, "invalid n");
            drawCurvedArrowDown(g2, x2, cy + r, rx, ry - r, "T[n]≠expected");
        }

        private void drawTribonacci(Graphics2D g2) {
            int w  = getWidth();
            int h  = getHeight();
            int r  = 22;
            int cy = 60;
            int ry = 138;

            int pad  = 44;
            int gap  = (w - pad * 2 - r * 2 * 5) / 4;
            int step = r * 2 + gap;

            int x0 = pad + r;
            int x1 = x0 + step;
            int x2 = x1 + step;
            int x3 = x2 + step;
            int x4 = x3 + step;
            int rx = (x1 + x3) / 2;

            drawStartArrow(g2, x0 - r - 30, cy, x0 - r - 2, cy);

            drawNode(g2, x0, cy, r, "q₀", NODE_STROKE, false);
            drawNode(g2, x1, cy, r, "q₁", NODE_STROKE, false);
            drawNode(g2, x2, cy, r, "q₂", NODE_STROKE, false);
            drawNode(g2, x3, cy, r, "q₃", NODE_STROKE, false);
            drawNode(g2, x4, cy, r, "q₄", ACCEPT_STROKE, true);

            drawRejectNode(g2, rx, ry, r);

            drawArrow(g2, x0 + r + 2, cy, x1 - r - 2, cy);
            drawArrow(g2, x1 + r + 2, cy, x2 - r - 2, cy);
            drawArrow(g2, x2 + r + 2, cy, x3 - r - 2, cy);
            drawArrow(g2, x3 + r + 2, cy, x4 - r - 2, cy);

            drawSelfLoop(g2, x4, cy, r, "loop");

            drawLabelAbove(g2, (x0 + x1) / 2, cy - r - 4, "read 0");
            drawLabelAbove(g2, (x1 + x2) / 2, cy - r - 4, "read 0");
            drawLabelAbove(g2, (x2 + x3) / 2, cy - r - 4, "read 1");
            drawLabelAbove(g2, (x3 + x4) / 2, cy - r - 4, "T[n]=p₁+p₂+p₃");

            drawCurvedArrowDown(g2, x1, cy + r, rx, ry - r, "invalid n");
            drawCurvedArrowDown(g2, x3, cy + r, rx, ry - r, "T[n]≠expected");
        }


        private void drawNode(Graphics2D g2, int cx, int cy, int r, String label,
                              Color strokeColor, boolean doubleRing) {
            g2.setColor(NODE_FILL);
            g2.fillOval(cx - r, cy - r, r * 2, r * 2);
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(strokeColor);
            g2.drawOval(cx - r, cy - r, r * 2, r * 2);
            if (doubleRing) {
                int inner = 4;
                g2.drawOval(cx - r + inner, cy - r + inner, (r - inner) * 2, (r - inner) * 2);
            }
            g2.setFont(new Font("Georgia", Font.PLAIN, 11));
            g2.setColor(STATE_TEXT);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, cx - fm.stringWidth(label) / 2, cy + fm.getAscent() / 2 - 1);
        }

        private void drawRejectNode(Graphics2D g2, int cx, int cy, int r) {
            g2.setColor(REJECT_FILL);
            g2.fillOval(cx - r, cy - r, r * 2, r * 2);
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(REJECT_STROKE);
            g2.drawOval(cx - r, cy - r, r * 2, r * 2);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            g2.setColor(REJECT_STROKE);
            FontMetrics fm = g2.getFontMetrics();
            String lbl = "REJECT";
            g2.drawString(lbl, cx - fm.stringWidth(lbl) / 2, cy + fm.getAscent() / 2 - 1);
        }

        private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
            g2.setColor(ARROW_COL);
            g2.setStroke(new BasicStroke(1.1f));
            g2.drawLine(x1, y1, x2, y2);
            drawArrowHead(g2, x1, y1, x2, y2, ARROW_COL, 7);
        }

        private void drawStartArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
            g2.setColor(START_ARROW);
            g2.setStroke(new BasicStroke(1.3f));
            g2.drawLine(x1, y1, x2, y2);
            drawArrowHead(g2, x1, y1, x2, y2, START_ARROW, 7);
        }

        private void drawCurvedArrowDown(Graphics2D g2, int fromX, int fromY,
                                         int toX, int toY, String label) {
            int ctrlX = (fromX + toX) / 2 + (fromX < toX ? -20 : 20);
            int ctrlY = (fromY + toY) / 2 + 20;

            Path2D path = new Path2D.Float();
            path.moveTo(fromX, fromY);
            path.quadTo(ctrlX, ctrlY, toX, toY);

            g2.setColor(ARROW_COL);
            g2.setStroke(new BasicStroke(1.1f));
            g2.draw(path);

            double dx = toX - ctrlX;
            double dy = toY - ctrlY;
            double angle = Math.atan2(dy, dx);
            int aw = 7;
            int[] ax = {toX,
                (int)(toX - aw * Math.cos(angle - 0.42)),
                (int)(toX - aw * Math.cos(angle + 0.42))};
            int[] ay = {toY,
                (int)(toY - aw * Math.sin(angle - 0.42)),
                (int)(toY - aw * Math.sin(angle + 0.42))};
            g2.setColor(ARROW_COL);
            g2.fillPolygon(ax, ay, 3);
            int lx = (fromX + ctrlX + toX) / 3;
            int ly = (fromY + ctrlY + toY) / 3 + 2;
            drawLabel(g2, lx, ly, label);
        }

        private void drawArrowHead(Graphics2D g2, int x1, int y1, int x2, int y2,
                                   Color color, int size) {
            double angle = Math.atan2(y2 - y1, x2 - x1);
            int[] ax = {x2,
                (int)(x2 - size * Math.cos(angle - 0.4)),
                (int)(x2 - size * Math.cos(angle + 0.4))};
            int[] ay = {y2,
                (int)(y2 - size * Math.sin(angle - 0.4)),
                (int)(y2 - size * Math.sin(angle + 0.4))};
            g2.setColor(color);
            g2.fillPolygon(ax, ay, 3);
        }

        private void drawSelfLoop(Graphics2D g2, int cx, int cy, int r, String label) {
            g2.setColor(ARROW_COL);
            g2.setStroke(new BasicStroke(1.1f));
            int lx = cx + r - 3;
            int ly = cy - r + 3;
            g2.drawArc(lx - 10, ly - 18, 22, 22, 20, 300);
            g2.fillPolygon(
                new int[]{lx - 1, lx - 8, lx - 3},
                new int[]{ly - 17, ly - 13, ly - 7},
                3);
            drawLabel(g2, lx + 14, ly - 12, label);
        }

        private void drawLabelAbove(Graphics2D g2, int cx, int y, String text) {
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            g2.setColor(LABEL_COL);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(text, cx - fm.stringWidth(text) / 2, y);
        }

        private void drawLabel(Graphics2D g2, int cx, int cy, String text) {
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            g2.setColor(LABEL_COL);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(text, cx - fm.stringWidth(text) / 2, cy);
        }
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
        String[] cols = {"n", "Reason"};
        List<Object[]> rows = new ArrayList<>();
        for (int v = -2; v <= 0; v++)
            rows.add(new Object[]{v, v < 0 ? "Negative — invalid" : "Zero — invalid"});
        for (int v = 1; v < min; v++)
            rows.add(new Object[]{v, "Too few terms for this sequence"});

        JTable t = new JTable(rows.toArray(new Object[0][]), cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        t.setBackground(SURFACE);
        t.setForeground(SUBTLE);
        t.setFont(MONO);
        t.setRowHeight(19);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));

        DefaultTableCellRenderer cr = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                c.setBackground(row % 2 == 0 ? SURFACE : CARD);
                c.setForeground(col == 0 ? ERR : MUTED);
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        };
        t.setDefaultRenderer(Object.class, cr);

        JTableHeader header = t.getTableHeader();
        header.setBackground(new Color(13, 13, 13));
        header.setForeground(MUTED);
        header.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
            .setHorizontalAlignment(SwingConstants.LEFT);

        t.getColumnModel().getColumn(0).setPreferredWidth(55);
        t.getColumnModel().getColumn(1).setPreferredWidth(625);
        return t;
    }

    private JButton makeBtn(String text, boolean ghost) {
        JButton btn = new JButton(text);
        btn.setFont(SMALL);
        btn.setForeground(ghost ? MUTED : new Color(18, 18, 18));
        btn.setBackground(ghost ? SURFACE : ACCENT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ghost ? BORDER : ACCENT2),
            BorderFactory.createEmptyBorder(5, 14, 5, 14)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Color hoverBg = ghost ? new Color(22,22,22) : ACCENT2;
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hoverBg); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(ghost ? SURFACE : ACCENT); }
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
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return f;
    }
}