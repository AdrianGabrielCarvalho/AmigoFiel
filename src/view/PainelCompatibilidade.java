package view;

import control.SistemaController;
import model.Animal;
import model.Adotante;
import exceptions.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PainelCompatibilidade extends JPanel {
    private SistemaController controller;
    private JTable tabelaCompativeis;
    private DefaultTableModel tableModel;
    private JTextArea areaRecomendacao;

    public PainelCompatibilidade(SistemaController controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel superior - Busca
        JPanel painelBusca = criarPainelBusca();
        add(painelBusca, BorderLayout.NORTH);

        // Painel central - Resultados
        JPanel painelCentral = criarPainelCentral();
        add(painelCentral, BorderLayout.CENTER);
    }

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 150, 100), 2),
                "Buscar Compatibilidade"
        ));
        painel.setBackground(new Color(250, 245, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes
        final JTextField txtCpf = new JTextField(15);
        final JTextField txtAnimalId = new JTextField(10);

        JButton btnBuscarAdotante = new JButton("Buscar Adotante");
        JButton btnBuscarAnimal = new JButton("Buscar Animal");
        JButton btnVerificarCompatibilidade = new JButton("Verificar Compatibilidade");
        JButton btnListarCompativeis = new JButton("Listar Animais Compativeis");

        btnVerificarCompatibilidade.setBackground(new Color(200, 150, 100));
        btnVerificarCompatibilidade.setForeground(Color.BLACK);
        btnVerificarCompatibilidade.setFont(new Font("Arial", Font.BOLD, 12));

        btnListarCompativeis.setBackground(new Color(150, 200, 150));
        btnListarCompativeis.setForeground(Color.BLACK);
        btnListarCompativeis.setFont(new Font("Arial", Font.BOLD, 12));

        // Posicionamento
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("CPF do Adotante:"), gbc);

        gbc.gridx = 1;
        painel.add(txtCpf, gbc);

        gbc.gridx = 2;
        painel.add(btnBuscarAdotante, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("ID do Animal:"), gbc);

        gbc.gridx = 1;
        painel.add(txtAnimalId, gbc);

        gbc.gridx = 2;
        painel.add(btnBuscarAnimal, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        painel.add(btnVerificarCompatibilidade, gbc);

        gbc.gridx = 2; gbc.gridwidth = 1;
        painel.add(btnListarCompativeis, gbc);

        // Ações dos botões
        btnBuscarAdotante.addActionListener(e -> {
            String cpf = txtCpf.getText().trim();
            if (!cpf.isEmpty()) {
                try {
                    Adotante adotante = controller.buscarAdotantePorCpf(cpf);
                    JOptionPane.showMessageDialog(this,
                            "Adotante encontrado:\n\n" +
                                    "CPF: " + adotante.getCpf() + "\n" +
                                    "Nome: " + adotante.getNome() + "\n" +
                                    "Moradia: " + adotante.getTipoMoradia() + "\n" +
                                    "Preferência: " + (adotante.getPreferenciaPorte() != null ?
                                    adotante.getPreferenciaPorte() : "Qualquer"),
                            "Adotante Encontrado", JOptionPane.INFORMATION_MESSAGE);
                } catch (AdotanteNaoEncontradoException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Adotante não encontrado:\n" + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

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
                                    "Porte: " + animal.getPorte() + "\n" +
                                    "Temperamento: " + animal.getTemperamento(),
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

        btnVerificarCompatibilidade.addActionListener(e -> {
            try {
                String cpf = txtCpf.getText().trim();
                String idStr = txtAnimalId.getText().trim();

                if (cpf.isEmpty() || idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Preencha ambos os campos!",
                            "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int animalId = Integer.parseInt(idStr);
                Animal animal = controller.buscarAnimalPorId(animalId);
                Adotante adotante = controller.buscarAdotantePorCpf(cpf);

                String recomendacao = controller.verificarCompatibilidade(animal, adotante);
                areaRecomendacao.setText(recomendacao);
                areaRecomendacao.setCaretPosition(0);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "ID do animal inválido. Digite um número.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (AnimalNaoEncontradoException | AdotanteNaoEncontradoException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro interno:\n" + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnListarCompativeis.addActionListener(e -> {
            String cpf = txtCpf.getText().trim();
            if (cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Digite o CPF do adotante primeiro!",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                List<Animal> animaisCompativeis = controller.getAnimaisCompativeis(cpf);
                atualizarTabelaCompativeis(animaisCompativeis);

                if (animaisCompativeis.isEmpty()) {
                    areaRecomendacao.setText("Nenhum animal compatível encontrado para este adotante.");
                } else {
                    areaRecomendacao.setText("Encontrados " + animaisCompativeis.size() +
                            " animais compativeis!");
                }

            } catch (AdotanteNaoEncontradoException ex) {
                JOptionPane.showMessageDialog(this,
                        "Adotante não encontrado:\n" + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao buscar animais compativeis:\n" + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return painel;
    }

    private JPanel criarPainelCentral() {
        JPanel painel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Painel esquerdo - Tabela de animais compativeis
        JPanel painelEsquerdo = new JPanel(new BorderLayout());
        painelEsquerdo.setBorder(BorderFactory.createTitledBorder("Animais Compativeis"));

        String[] colunas = {"ID", "Nome", "Espécie", "Porte", "Temperamento"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaCompativeis = new JTable(tableModel);
        tabelaCompativeis.setRowHeight(25);
        tabelaCompativeis.setFont(new Font("Arial", Font.PLAIN, 11));

        JScrollPane scrollTabela = new JScrollPane(tabelaCompativeis);
        painelEsquerdo.add(scrollTabela, BorderLayout.CENTER);

        // Painel direito - Recomendação detalhada
        JPanel painelDireito = new JPanel(new BorderLayout());
        painelDireito.setBorder(BorderFactory.createTitledBorder("Análise Detalhada"));

        areaRecomendacao = new JTextArea();
        areaRecomendacao.setEditable(false);
        areaRecomendacao.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaRecomendacao.setBackground(new Color(250, 250, 250));
        areaRecomendacao.setText("Use os campos acima para verificar compatibilidade...\n\n" +
                "1. Digite o CPF do adotante\n" +
                "2. Digite o ID do animal\n" +
                "3. Clique em 'Verificar Compatibilidade'\n\n" +
                "Ou clique em 'Listar Animais Compativeis' para ver\n" +
                "todos os animais adequados para um adotante.");

        JScrollPane scrollRecomendacao = new JScrollPane(areaRecomendacao);
        scrollRecomendacao.setPreferredSize(new Dimension(400, 300));

        painelDireito.add(scrollRecomendacao, BorderLayout.CENTER);

        painel.add(painelEsquerdo);
        painel.add(painelDireito);

        return painel;
    }

    private void atualizarTabelaCompativeis(List<Animal> animais) {
        tableModel.setRowCount(0);
        for (Animal animal : animais) {
            Object[] row = {
                    animal.getId(),
                    animal.getNome(),
                    animal.getEspecie(),
                    animal.getPorte(),
                    animal.getTemperamento()
            };
            tableModel.addRow(row);
        }
    }
}