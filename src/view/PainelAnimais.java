package view;

import control.SistemaController;
import model.Animal;
import exceptions.AnimalNaoEncontradoException;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PainelAnimais extends JPanel {
    private SistemaController controller;
    private JTable tabelaAnimais;
    private DefaultTableModel tableModel;

    public PainelAnimais(SistemaController controller) {
        this.controller = controller;
        initComponents();
        carregarAnimais();
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
        JLabel lblStatus = new JLabel("Total de animais: 0");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        add(lblStatus, BorderLayout.SOUTH);
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 200, 100), 2),
                "Cadastrar Novo Animal"
        ));
        painel.setBackground(new Color(255, 255, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes
        final JTextField txtNome = new JTextField(20);
        final JComboBox<Animal.Especie> comboEspecie = new JComboBox<>(Animal.Especie.values());
        final JSpinner spinnerIdade = new JSpinner(new SpinnerNumberModel(1, 0, 30, 1));
        final JComboBox<Animal.Porte> comboPorte = new JComboBox<>(Animal.Porte.values());
        final JComboBox<Animal.Temperamento> comboTemperamento = new JComboBox<>(Animal.Temperamento.values());

        JButton btnCadastrar = new JButton("Cadastrar Animal");
        btnCadastrar.setBackground(new Color(100, 200, 100));
        btnCadastrar.setForeground(Color.BLACK);
        btnCadastrar.setFont(new Font("Arial", Font.BOLD, 12));

        // Posicionamento
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        painel.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Espécie:"), gbc);

        gbc.gridx = 1;
        painel.add(comboEspecie, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Idade:"), gbc);

        gbc.gridx = 1;
        painel.add(spinnerIdade, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        painel.add(new JLabel("Porte:"), gbc);

        gbc.gridx = 1;
        painel.add(comboPorte, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        painel.add(new JLabel("Temperamento:"), gbc);

        gbc.gridx = 1;
        painel.add(comboTemperamento, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        painel.add(btnCadastrar, gbc);

        // Ação do botão
        btnCadastrar.addActionListener(e -> {
            try {
                String nome = txtNome.getText().trim();
                Animal.Especie especie = (Animal.Especie) comboEspecie.getSelectedItem();
                int idade = (int) spinnerIdade.getValue();
                Animal.Porte porte = (Animal.Porte) comboPorte.getSelectedItem();
                Animal.Temperamento temperamento = (Animal.Temperamento) comboTemperamento.getSelectedItem();

                Animal animal = controller.cadastrarAnimal(nome, especie, idade, porte, temperamento);

                JOptionPane.showMessageDialog(this,
                        "Animal cadastrado com sucesso!\n\n" +
                                "ID: " + animal.getId() + "\n" +
                                "Nome: " + animal.getNome() + "\n" +
                                "Status: " + animal.getStatus(),
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // Limpar campos
                txtNome.setText("");
                spinnerIdade.setValue(1);
                carregarAnimais();

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
        JButton btnListarDisponiveis = new JButton("Listar Disponíveis");
        JButton btnFiltrarCaes = new JButton("Apenas Cães");
        JButton btnFiltrarGatos = new JButton("Apenas Gatos");
        JButton btnRemover = new JButton("Remover Selecionado");
        JButton btnAtualizar = new JButton("Atualizar");

        // Estilizar botões
        Color corPadrao = new Color(220, 220, 230);
        btnListarTodos.setBackground(corPadrao);
        btnListarDisponiveis.setBackground(new Color(150, 220, 150));
        btnFiltrarCaes.setBackground(corPadrao);
        btnFiltrarGatos.setBackground(corPadrao);
        btnRemover.setBackground(new Color(255, 200, 200));
        btnAtualizar.setBackground(new Color(200, 220, 255));

        painelBotoes.add(btnListarTodos);
        painelBotoes.add(btnListarDisponiveis);
        painelBotoes.add(btnFiltrarCaes);
        painelBotoes.add(btnFiltrarGatos);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnAtualizar);

        // Tabela
        String[] colunas = {"ID", "Nome", "Espécie", "Idade", "Porte", "Temperamento", "Status"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                return String.class;
            }
        };

        tabelaAnimais = new JTable(tableModel);
        tabelaAnimais.setRowHeight(30);
        tabelaAnimais.setFont(new Font("Arial", Font.PLAIN, 12));
        tabelaAnimais.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabelaAnimais.setSelectionBackground(new Color(200, 220, 255));

        configurarAlinhamentoTabela();

        JScrollPane scrollPane = new JScrollPane(tabelaAnimais);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Animais"));

        btnListarTodos.addActionListener(e -> carregarAnimais());
        btnListarDisponiveis.addActionListener(e -> carregarAnimaisDisponiveis());
        btnFiltrarCaes.addActionListener(e -> filtrarPorEspecie(Animal.Especie.CAO));
        btnFiltrarGatos.addActionListener(e -> filtrarPorEspecie(Animal.Especie.GATO));
        btnAtualizar.addActionListener(e -> carregarAnimais());

        btnRemover.addActionListener(e -> removerAnimalSelecionado());

        painel.add(painelBotoes, BorderLayout.NORTH);
        painel.add(scrollPane, BorderLayout.CENTER);

        return painel;
    }

    private void configurarAlinhamentoTabela() {
        DefaultTableCellRenderer rendererEsquerda = new DefaultTableCellRenderer();
        rendererEsquerda.setHorizontalAlignment(SwingConstants.LEFT);

        for (int i = 0; i < tabelaAnimais.getColumnCount(); i++) {
            tabelaAnimais.getColumnModel().getColumn(i).setCellRenderer(rendererEsquerda);
        }

        tabelaAnimais.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        tabelaAnimais.getColumnModel().getColumn(1).setPreferredWidth(150);  // Nome
        tabelaAnimais.getColumnModel().getColumn(2).setPreferredWidth(80);   // Espécie
        tabelaAnimais.getColumnModel().getColumn(3).setPreferredWidth(70);   // Idade
        tabelaAnimais.getColumnModel().getColumn(4).setPreferredWidth(80);   // Porte
        tabelaAnimais.getColumnModel().getColumn(5).setPreferredWidth(100);  // Temperamento
        tabelaAnimais.getColumnModel().getColumn(6).setPreferredWidth(100);  // Status
    }

    private void carregarAnimais() {
        try {
            List<Animal> animais = controller.getTodosAnimais();
            atualizarTabela(animais);
            atualizarContador(animais.size());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar animais:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarAnimaisDisponiveis() {
        try {
            List<Animal> animais = controller.listarAnimaisDisponiveis();
            atualizarTabela(animais);
            atualizarContador(animais.size());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar animais disponíveis:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarPorEspecie(Animal.Especie especie) {
        try {
            List<Animal> animais = controller.filtrarAnimaisPorEspecie(especie);
            atualizarTabela(animais);
            atualizarContador(animais.size());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao filtrar animais:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerAnimalSelecionado() {
        int linha = tabelaAnimais.getSelectedRow();
        if (linha >= 0) {
            int id = (int) tableModel.getValueAt(linha, 0);
            String nome = (String) tableModel.getValueAt(linha, 1);
            String status = (String) tableModel.getValueAt(linha, 6); // Obter status da tabela

            if (status.contains("ADOTADO") || status.contains("🔴")) {
                JOptionPane.showMessageDialog(this,
                        "Não é possível remover um animal que já foi adotado!",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente remover o animal?\n\n" +
                            "ID: " + id + "\n" +
                            "Nome: " + nome + "\n\n" +
                            "Esta ação não pode ser desfeita!",
                    "Confirmar Remoção",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean sucesso = controller.removerAnimal(id);
                    if (sucesso) {
                        JOptionPane.showMessageDialog(this,
                                "Animal removido com sucesso!",
                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        carregarAnimais();
                    }
                } catch (AnimalNaoEncontradoException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Animal não encontrado:\n" + ex.getMessage(),
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
                    "Selecione um animal na tabela para remover.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void atualizarTabela(List<Animal> animais) {
        tableModel.setRowCount(0);
        for (Animal animal : animais) {
            String especieTexto = animal.getEspecie() == Animal.Especie.CAO ? "Cão" : "Gato";
            String statusCompleto = "";

            switch(animal.getStatus()) {
                case DISPONIVEL -> statusCompleto = "DISPONÍVEL";
                case ADOTADO -> statusCompleto = "ADOTADO";
                case EM_PROCESSO -> statusCompleto = "EM PROCESSO";
            }
            Object[] row = {
                    animal.getId(),
                    animal.getNome(),
                    especieTexto,
                    animal.getIdade() + " anos",
                    animal.getPorte(),
                    animal.getTemperamento(),
                    statusCompleto
            };
            tableModel.addRow(row);
        }

        // Colorir as linhas baseadas no status
        colorirLinhasPorStatus();
    }

    private void colorirLinhasPorStatus() {
        tabelaAnimais.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Obter o valor da coluna Status (coluna 6)
                String status = (String) table.getValueAt(row, 6);

                // Definir cores baseadas no status
                if (!isSelected) {
                    if (status.contains("ADOTADO") || status.contains("🔴")) {
                        c.setBackground(new Color(255, 230, 230)); // Vermelho claro para adotados
                        c.setForeground(Color.DARK_GRAY);
                    } else if (status.contains("DISPONÍVEL") || status.contains("🟢")) {
                        c.setBackground(new Color(230, 255, 230)); // Verde claro para disponíveis
                        c.setForeground(Color.DARK_GRAY);
                    } else if (status.contains("EM PROCESSO") || status.contains("🟡")) {
                        c.setBackground(new Color(255, 255, 200)); // Amarelo claro para em processo
                        c.setForeground(Color.DARK_GRAY);
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                } else {
                    // Quando selecionado, manter a cor padrão de seleção
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }

                return c;
            }
        });
    }

    private void atualizarContador(int total) {
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().contains("Total de animais:")) {
                    label.setText("Total de animais: " + total);
                }
            }
        }
    }
}