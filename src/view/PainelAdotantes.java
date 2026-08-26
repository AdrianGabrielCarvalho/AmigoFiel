package view;

import control.SistemaController;
import model.Adotante;
import model.Animal;
import exceptions.AdotanteNaoEncontradoException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PainelAdotantes extends JPanel {
    private SistemaController controller;
    private JTable tabelaAdotantes;
    private DefaultTableModel tableModel;

    public PainelAdotantes(SistemaController controller) {
        this.controller = controller;
        initComponents();
        carregarAdotantes();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel superior - Formulário
        JPanel painelForm = criarPainelFormulario();
        add(painelForm, BorderLayout.NORTH);

        // Painel central - Tabela
        JPanel painelCentral = criarPainelTabela();
        add(painelCentral, BorderLayout.CENTER);

        // Painel inferior - Status
        JLabel lblStatus = new JLabel("Total de adotantes: 0");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        add(lblStatus, BorderLayout.SOUTH);
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
                "Cadastrar Novo Adotante"
        ));
        painel.setBackground(new Color(255, 255, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes - declarados como finais ou efetivamente finais
        final JTextField txtCpf = new JTextField(20);
        final JTextField txtNome = new JTextField(20);
        final JTextField txtTelefone = new JTextField(20);
        final JComboBox<Adotante.TipoMoradia> comboMoradia = new JComboBox<>(Adotante.TipoMoradia.values());
        final JComboBox<Animal.Porte> comboPreferencia = new JComboBox<>(Animal.Porte.values());

        // Configuração inicial
        comboPreferencia.insertItemAt(null, 0);
        comboPreferencia.setSelectedIndex(0);

        JButton btnCadastrar = new JButton("Cadastrar Adotante");
        btnCadastrar.setBackground(new Color(100, 150, 200));
        btnCadastrar.setForeground(Color.BLACK);
        btnCadastrar.setFont(new Font("Arial", Font.BOLD, 12));

        // Posicionamento
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("CPF:"), gbc);

        gbc.gridx = 1;
        painel.add(txtCpf, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        painel.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Telefone:"), gbc);

        gbc.gridx = 1;
        painel.add(txtTelefone, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        painel.add(new JLabel("Tipo de Moradia:"), gbc);

        gbc.gridx = 1;
        painel.add(comboMoradia, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        painel.add(new JLabel("Preferência de Porte:"), gbc);

        gbc.gridx = 1;
        painel.add(comboPreferencia, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        painel.add(btnCadastrar, gbc);

        // Ação do botão
        btnCadastrar.addActionListener(e -> {
            try {
                String cpf = txtCpf.getText().trim();
                String nome = txtNome.getText().trim();
                String telefone = txtTelefone.getText().trim();
                Adotante.TipoMoradia moradia = (Adotante.TipoMoradia) comboMoradia.getSelectedItem();
                Animal.Porte preferencia = (Animal.Porte) comboPreferencia.getSelectedItem();

                controller.cadastrarAdotante(cpf, nome, telefone, moradia, preferencia);

                JOptionPane.showMessageDialog(this,
                        "Adotante cadastrado com sucesso!\n\n" +
                                "Nome: " + nome + "\n" +
                                "CPF: " + cpf + "\n" +
                                "Telefone: " + telefone,
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // Limpar campos
                txtCpf.setText("");
                txtNome.setText("");
                txtTelefone.setText("");
                comboPreferencia.setSelectedIndex(0);
                carregarAdotantes();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro de validação:\n" + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro interno:\n" + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return painel;
    }

    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout());

        // Botões de ação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.setBackground(new Color(255, 255, 255));

        JButton btnListarTodos = new JButton("Listar Todos");
        JButton btnBuscarPorCpf = new JButton("Buscar por CPF");
        JButton btnRemover = new JButton("Remover Selecionado");
        JButton btnAtualizar = new JButton("Atualizar");

        // Estilizar botões
        Color corPadrao = new Color(220, 220, 230);
        btnListarTodos.setBackground(corPadrao);
        btnBuscarPorCpf.setBackground(corPadrao);
        btnRemover.setBackground(new Color(255, 200, 200));
        btnAtualizar.setBackground(new Color(200, 220, 255));

        painelBotoes.add(btnListarTodos);
        painelBotoes.add(btnBuscarPorCpf);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnAtualizar);

        // Tabela
        String[] colunas = {"CPF", "Nome", "Telefone", "Moradia", "Preferência"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaAdotantes = new JTable(tableModel);
        tabelaAdotantes.setRowHeight(30);
        tabelaAdotantes.setFont(new Font("Arial", Font.PLAIN, 12));
        tabelaAdotantes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabelaAdotantes.setSelectionBackground(new Color(200, 220, 255));

        JScrollPane scrollPane = new JScrollPane(tabelaAdotantes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Adotantes"));

        // Ações dos botões
        btnListarTodos.addActionListener(e -> carregarAdotantes());
        btnAtualizar.addActionListener(e -> carregarAdotantes());

        btnBuscarPorCpf.addActionListener(e -> {
            String cpf = JOptionPane.showInputDialog(this, "Digite o CPF:");
            if (cpf != null && !cpf.trim().isEmpty()) {
                buscarAdotante(cpf);
            }
        });

        btnRemover.addActionListener(e -> removerAdotanteSelecionado());

        painel.add(painelBotoes, BorderLayout.NORTH);
        painel.add(scrollPane, BorderLayout.CENTER);

        return painel;
    }

    private void carregarAdotantes() {
        try {
            List<Adotante> adotantes = controller.getTodosAdotantes();
            atualizarTabela(adotantes);
            atualizarContador(adotantes.size());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar adotantes:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarAdotante(String cpf) {
        try {
            Adotante adotante = controller.buscarAdotantePorCpf(cpf);
            tableModel.setRowCount(0);
            adicionarAdotanteNaTabela(adotante);
            atualizarContador(1);
        } catch (AdotanteNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this,
                    "Adotante não encontrado:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao buscar adotante:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerAdotanteSelecionado() {
        int linha = tabelaAdotantes.getSelectedRow();
        if (linha >= 0) {
            String cpf = (String) tableModel.getValueAt(linha, 0);
            String nome = (String) tableModel.getValueAt(linha, 1);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente remover o adotante?\n\n" +
                            "CPF: " + cpf + "\n" +
                            "Nome: " + nome + "\n\n" +
                            "Esta ação não pode ser desfeita!",
                    "Confirmar Remoção",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean sucesso = controller.removerAdotante(cpf);
                    if (sucesso) {
                        JOptionPane.showMessageDialog(this,
                                "Adotante removido com sucesso!",
                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        carregarAdotantes();
                    }
                } catch (AdotanteNaoEncontradoException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Adotante não encontrado:\n" + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalStateException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Não é possível remover:\n" + ex.getMessage(),
                            "Atenção", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao remover:\n" + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione um adotante na tabela para remover.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void atualizarTabela(List<Adotante> adotantes) {
        tableModel.setRowCount(0);
        for (Adotante adotante : adotantes) {
            String moradiaSimbolo = (adotante.getTipoMoradia() == Adotante.TipoMoradia.CASA) ? "CASA" : "APT";
            String preferencia = adotante.getPreferenciaPorte() != null ?
                    adotante.getPreferenciaPorte().toString() : "Qualquer";

            Object[] row = {
                    adotante.getCpf(),
                    adotante.getNome(),
                    adotante.getTelefone(),
                    moradiaSimbolo,
                    preferencia
            };
            tableModel.addRow(row);
        }
    }

    private void adicionarAdotanteNaTabela(Adotante adotante) {
        String moradiaSimbolo = (adotante.getTipoMoradia() == Adotante.TipoMoradia.CASA) ? "CASA" : "APT";
        String preferencia = adotante.getPreferenciaPorte() != null ?
                adotante.getPreferenciaPorte().toString() : "Qualquer";

        Object[] row = {
                adotante.getCpf(),
                adotante.getNome(),
                adotante.getTelefone(),
                moradiaSimbolo,
                preferencia
        };
        tableModel.addRow(row);
    }

    private void atualizarContador(int total) {
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().contains("Total de adotantes:")) {
                    label.setText("Total de adotantes: " + total);
                }
            }
        }
    }
}