package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JanelaPrincipal extends JFrame {
    private JTabbedPane tabbedPane;
    private control.SistemaController sistemaController;

    public JanelaPrincipal() {
        super("Sistema de Adoção de Pets");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        try {
            sistemaController = new control.SistemaController();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao iniciar sistema: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        initComponents();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salvarDadosESair();
            }
        });
    }

    private void initComponents() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSalvar = new JMenuItem("Salvar Dados");
        JMenuItem itemSair = new JMenuItem("Sair");

        itemSalvar.addActionListener(e -> {
            try {
                sistemaController.salvarTodosDados();
                JOptionPane.showMessageDialog(this,
                        "Dados salvos com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar dados: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        itemSair.addActionListener(e -> salvarDadosESair());

        menuArquivo.add(itemSalvar);
        menuArquivo.addSeparator();
        menuArquivo.add(itemSair);
        menuBar.add(menuArquivo);
        setJMenuBar(menuBar);

        tabbedPane = new JTabbedPane();

        PainelAnimais painelAnimais = new PainelAnimais(sistemaController);
        PainelAdotantes painelAdotantes = new PainelAdotantes(sistemaController);
        PainelAdocoes painelAdocoes = new PainelAdocoes(sistemaController);
        PainelCompatibilidade painelCompatibilidade = new PainelCompatibilidade(sistemaController);

        tabbedPane.addTab("Animais", painelAnimais);
        tabbedPane.addTab("Adotantes", painelAdotantes);
        tabbedPane.addTab("Adoções", painelAdocoes);
        tabbedPane.addTab("Compatibilidade", painelCompatibilidade);

        add(tabbedPane, BorderLayout.CENTER);

        JPanel painelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblStatus = new JLabel("Sistema de Adoção de Pets");
        painelStatus.add(lblStatus);
        add(painelStatus, BorderLayout.SOUTH);
    }

    private void salvarDadosESair() {
        sistemaController.salvarTodosDados();
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente sair do sistema?",
                "Confirmar Saída",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JanelaPrincipal janela = new JanelaPrincipal();
            janela.setVisible(true);
        });
    }
}