package view;

import control.SistemaController;
import model.Adocao;
import model.Animal;
import model.Adotante;
import exceptions.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PainelAdocoes extends JPanel {
    private SistemaController controller;
    private JTable tabelaAdocoes;
    private DefaultTableModel tableModel;

    public PainelAdocoes(SistemaController controller) {
        this.controller = controller;
        initComponents();
        carregarAdocoes();
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
        JLabel lblStatus = new JLabel("Total de adoções: 0");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        add(lblStatus, BorderLayout.SOUTH);
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 200, 150), 2),
                "Registrar Nova Adoção"
        ));
        painel.setBackground(new Color(245, 250, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes
        final JTextField txtAnimalId = new JTextField(10);
        final JTextField txtCpfAdotante = new JTextField(15);

        JButton btnBuscarAnimal = new JButton("Buscar Animal");
        JButton btnBuscarAdotante = new JButton("Buscar Adotante");
        JButton btnRegistrar = new JButton("Registrar Adoção");

        btnRegistrar.setBackground(new Color(100, 200, 150));
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 12));

        // Posicionamento
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("ID do Animal:"), gbc);

        gbc.gridx = 1;
        painel.add(txtAnimalId, gbc);

        gbc.gridx = 2;
        painel.add(btnBuscarAnimal, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("CPF do Adotante:"), gbc);

        gbc.gridx = 1;
        painel.add(txtCpfAdotante, gbc);

        gbc.gridx = 2;
        painel.add(btnBuscarAdotante, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        painel.add(btnRegistrar, gbc);

        // Ações dos botões
        btnBuscarAnimal.addActionListener(e -> {
            String idStr = txtAnimalId.getText().trim();
            if (!idStr.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    Animal animal = controller.buscarAnimalPorId(id);
                    JOptionPane.showMessageDialog(this,
                            "Animal encontrado:\n\n" +
                                    "ID: " + animal.getId() + "\n" +
                                    "Nome: " + animal.getNome() + "\n" +
                                    "Espécie: " + animal.getEspecie() + "\n" +
                                    "Status: " + animal.getStatus(),
                            "Animal Encontrado", JOptionPane.INFORMATION_MESSAGE);
                } catch (AnimalNaoEncontradoException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Animal não encontrado:\n" + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "ID inválido. Digite um número.",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnBuscarAdotante.addActionListener(e -> {
            String cpf = txtCpfAdotante.getText().trim();
            if (!cpf.isEmpty()) {
                try {
                    Adotante adotante = controller.buscarAdotantePorCpf(cpf);
                    JOptionPane.showMessageDialog(this,
                            "Adotante encontrado:\n\n" +
                                    "CPF: " + adotante.getCpf() + "\n" +
                                    "Nome: " + adotante.getNome() + "\n" +
                                    "Telefone: " + adotante.getTelefone() + "\n" +
                                    "Moradia: " + adotante.getTipoMoradia(),
                            "Adotante Encontrado", JOptionPane.INFORMATION_MESSAGE);
                } catch (AdotanteNaoEncontradoException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Adotante não encontrado:\n" + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnRegistrar.addActionListener(e -> {
            try {
                String idStr = txtAnimalId.getText().trim();
                String cpf = txtCpfAdotante.getText().trim();

                if (idStr.isEmpty() || cpf.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Preencha todos os campos!",
                            "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int animalId = Integer.parseInt(idStr);
                Adocao adocao = controller.registrarAdocao(animalId, cpf);

                JOptionPane.showMessageDialog(this,
                        "Adoção registrada com sucesso!\n\n" +
                                "Protocolo: " + adocao.getProtocolo() + "\n" +
                                "Animal: " + adocao.getAnimalSelecionado().getNome() + "\n" +
                                "Adotante: " + adocao.getAdotanteResponsavel().getNome() + "\n" +
                                "Data: " + adocao.getData() + "\n" +
                                "Status: " + (adocao.isConcluida() ? "Concluída" : "Em processo") + "\n\n" +
                                "Nota: Quando esta adoção for concluída, o adotante será removido da lista de adotantes.",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // Limpar campos
                txtAnimalId.setText("");
                txtCpfAdotante.setText("");
                carregarAdocoes();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "ID do animal inválido. Digite um número.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (AnimalNaoEncontradoException | AdotanteNaoEncontradoException |
                     AdocaoInvalidaException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao registrar adoção:\n" + ex.getMessage(),
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
        painelBotoes.setBackground(new Color(240, 240, 245));

        JButton btnListarTodas = new JButton("Listar Todas");
        JButton btnConcluir = new JButton("Concluir Adoção");
        JButton btnRemover = new JButton("Cancelar Adoção");
        JButton btnAtualizar = new JButton("Atualizar");

        // Estilizar botões
        Color corPadrao = new Color(220, 220, 230);
        btnListarTodas.setBackground(corPadrao);
        btnConcluir.setBackground(new Color(150, 255, 150));
        btnRemover.setBackground(new Color(255, 200, 200));
        btnAtualizar.setBackground(new Color(200, 220, 255));

        painelBotoes.add(btnListarTodas);
        painelBotoes.add(btnConcluir);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnAtualizar);

        // Tabela
        String[] colunas = {"Protocolo", "Animal", "Adotante", "Data", "Status"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaAdocoes = new JTable(tableModel);
        tabelaAdocoes.setRowHeight(30);
        tabelaAdocoes.setFont(new Font("Arial", Font.PLAIN, 12));
        tabelaAdocoes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabelaAdocoes.setSelectionBackground(new Color(200, 255, 200));

        JScrollPane scrollPane = new JScrollPane(tabelaAdocoes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Adoções"));

        // Ações dos botões
        btnListarTodas.addActionListener(e -> carregarAdocoes());
        btnAtualizar.addActionListener(e -> carregarAdocoes());

        btnConcluir.addActionListener(e -> concluirAdocaoSelecionada());

        btnRemover.addActionListener(e -> cancelarAdocaoSelecionada());

        painel.add(painelBotoes, BorderLayout.NORTH);
        painel.add(scrollPane, BorderLayout.CENTER);

        return painel;
    }

    private void carregarAdocoes() {
        try {
            List<Adocao> adocoes = controller.getTodasAdocoes();
            atualizarTabela(adocoes);
            atualizarContador(adocoes.size());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar adoções:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void concluirAdocaoSelecionada() {
        int linha = tabelaAdocoes.getSelectedRow();
        if (linha >= 0) {
            String protocolo = (String) tableModel.getValueAt(linha, 0);
            String animal = (String) tableModel.getValueAt(linha, 1);
            String adotante = (String) tableModel.getValueAt(linha, 2);
            String status = (String) tableModel.getValueAt(linha, 4);

            if (status.contains("Concluída")) {
                JOptionPane.showMessageDialog(this,
                        "Esta adoção já está concluída.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja concluir esta adoção?\n\n" +
                            "Protocolo: " + protocolo + "\n" +
                            "Animal: " + animal + "\n" +
                            "Adotante: " + adotante + "\n\n" +
                            "Após conclusão:\n" +
                            "1. O animal será marcado como ADOTADO\n" +
                            "2. O adotante será REMOVIDO da lista de adotantes\n" +
                            "3. O adotante NÃO poderá adotar outro animal",
                    "Confirmar Conclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean sucesso = controller.concluirAdocao(protocolo);
                    if (sucesso) {
                        JOptionPane.showMessageDialog(this,
                                "Adoção concluída com sucesso!\n\n" +
                                        "O animal '" + animal + "' foi marcado como ADOTADO.\n" +
                                        "O adotante '" + adotante + "' foi removido da lista de adotantes.",
                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        carregarAdocoes();
                    }
                } catch (AdocaoInvalidaException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao concluir adoção:\n" + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma adoção na tabela para concluir.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cancelarAdocaoSelecionada() {
        int linha = tabelaAdocoes.getSelectedRow();
        if (linha >= 0) {
            String protocolo = (String) tableModel.getValueAt(linha, 0);
            String status = (String) tableModel.getValueAt(linha, 4);

            if (status.contains("Concluída")) {
                JOptionPane.showMessageDialog(this,
                        "Não é possível cancelar uma adoção já concluída.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja cancelar esta adoção?\n\n" +
                            "Protocolo: " + protocolo + "\n\n" +
                            "O animal voltará para a lista de disponíveis.",
                    "Confirmar Cancelamento",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean sucesso = controller.removerAdocao(protocolo);
                    if (sucesso) {
                        JOptionPane.showMessageDialog(this,
                                "Adoção cancelada com sucesso!",
                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        carregarAdocoes();
                    }
                } catch (AdocaoInvalidaException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao cancelar adoção:\n" + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma adoção na tabela para cancelar.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void atualizarTabela(List<Adocao> adocoes) {
        tableModel.setRowCount(0);
        for (Adocao adocao : adocoes) {
            String statusTexto = adocao.isConcluida() ? "Concluída" : "Em processo";

            Object[] row = {
                    adocao.getProtocolo(),
                    adocao.getAnimalSelecionado().getNome(),
                    adocao.getAdotanteResponsavel().getNome(),
                    adocao.getData().toString(),
                    statusTexto
            };
            tableModel.addRow(row);
        }
    }

    private void atualizarContador(int total) {
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().contains("Total de adoções:")) {
                    label.setText("Total de adoções: " + total);
                }
            }
        }
    }
}