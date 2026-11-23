import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class TelaPrincipal extends JFrame {

    private JTextField txtEmpresa;
    private JTextField txtFuncionarios;

    private JCheckBox chkDados;
    private JCheckBox chkRecrutamento;
    private JCheckBox chkAdmissao;
    private JCheckBox chkFerias;
    private JCheckBox chkDesligamento;
    private JCheckBox chkVisaoGeral;

    private JTextArea txtResultado;
    private JButton btnRecomendar;

    public TelaPrincipal() {

        setTitle("HR Digital Transformation Advisor");
        setSize(800, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ----------------------------------
        // Painel Superior: dados da empresa
        // ----------------------------------

        JPanel dados = new JPanel(new GridLayout(2, 2, 5, 5));

        dados.add(new JLabel("Nome da empresa:"));
        txtEmpresa = new JTextField();
        dados.add(txtEmpresa);

        dados.add(new JLabel("Quantidade de funcionários:"));
        txtFuncionarios = new JTextField();
        dados.add(txtFuncionarios);

        // ----------------------------------
        // Painel dos problemas (caixinhas)
        // ----------------------------------

        JPanel problemas = new JPanel(new GridLayout(3, 2, 5, 5));
        problemas.setBorder(BorderFactory.createTitledBorder("Problemas de RH encontrados"));

        chkDados = new JCheckBox("Dados dos colaboradores dispersos e desatualizados");
        chkRecrutamento = new JCheckBox("Contratação sem padrão entre as unidades");
        chkAdmissao = new JCheckBox("Admissão com muita papelada");
        chkFerias = new JCheckBox("Férias e ausências controladas à mão");
        chkDesligamento = new JCheckBox("Desligamento sem processo padrão");
        chkVisaoGeral = new JCheckBox("Diretoria sem visão geral do RH");

        problemas.add(chkDados);
        problemas.add(chkRecrutamento);
        problemas.add(chkAdmissao);
        problemas.add(chkFerias);
        problemas.add(chkDesligamento);
        problemas.add(chkVisaoGeral);

        JPanel topo = new JPanel(new BorderLayout(5, 10));
        topo.add(dados, BorderLayout.NORTH);
        topo.add(problemas, BorderLayout.CENTER);

        painel.add(topo, BorderLayout.NORTH);

        // ----------------------------------
        // Área de Resultado
        // ----------------------------------

        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);
        txtResultado.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(txtResultado);
        painel.add(scroll, BorderLayout.CENTER);

        // ----------------------------------
        // Botão
        // ----------------------------------

        btnRecomendar = new JButton("Recomendar módulos SAP SuccessFactors");
        btnRecomendar.addActionListener((ActionEvent e) -> gerarRecomendacao());
        painel.add(btnRecomendar, BorderLayout.SOUTH);

        add(painel);
    }

    // ----------------------------------
    // Lógica: lê os dados e monta a recomendação
    // ----------------------------------

    private void gerarRecomendacao() {

        String empresa = txtEmpresa.getText().trim();
        int funcionarios;

        if (empresa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome da empresa.");
            return;
        }

        try {
            funcionarios = Integer.parseInt(txtFuncionarios.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Digite a quantidade de funcionários usando apenas números.");
            return;
        }

        List<String> modulos = new ArrayList<>();
        StringBuilder detalhes = new StringBuilder();

        if (chkDados.isSelected()) {
            registrar(modulos, detalhes,
                    "Dados dispersos e desatualizados",
                    "Employee Central",
                    "cria um cadastro único e atualizado de todos os colaboradores, acabando com as planilhas separadas.");
        }

        if (chkRecrutamento.isSelected()) {
            registrar(modulos, detalhes,
                    "Contratação sem padrão",
                    "Recruiting Management",
                    "padroniza a contratação em todas as unidades e permite medir o tempo e o custo de cada vaga.");
        }

        if (chkAdmissao.isSelected()) {
            registrar(modulos, detalhes,
                    "Admissão com muita papelada",
                    "Onboarding 2.0",
                    "digitaliza documentos e assinaturas e guia o novo colaborador nos primeiros dias.");
        }

        if (chkFerias.isSelected()) {
            registrar(modulos, detalhes,
                    "Férias e ausências manuais",
                    "Time Off",
                    "organiza os pedidos e as aprovações de férias e ausências de forma digital.");
        }

        if (chkDesligamento.isSelected()) {
            registrar(modulos, detalhes,
                    "Desligamento sem processo",
                    "Onboarding 2.0",
                    "organiza a saída do colaborador: devolução de equipamentos, retirada de acessos e pesquisa de desligamento.");
        }

        if (chkVisaoGeral.isSelected()) {
            registrar(modulos, detalhes,
                    "Diretoria sem visão geral do RH",
                    "Employee Central",
                    "centraliza os dados de todas as unidades, dando à diretoria uma visão do grupo inteiro.");
        }

        if (modulos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Marque pelo menos um problema de RH.");
            return;
        }

        StringBuilder resultado = new StringBuilder();

        resultado.append("Empresa: ").append(empresa)
                 .append(" (").append(funcionarios).append(" funcionários)\n\n");

        resultado.append("MÓDULOS RECOMENDADOS:\n");
        for (String modulo : modulos) {
            resultado.append(" - ").append(modulo).append("\n");
        }

        resultado.append("\nPOR QUE CADA UM:\n");
        resultado.append(detalhes);

        if (funcionarios >= 1000) {
            resultado.append("\nObservação: com ").append(funcionarios)
                     .append(" funcionários, recomenda-se implantar em fases, começando por uma unidade piloto.\n");
        } else {
            resultado.append("\nObservação: por ser uma empresa menor, a implantação pode ser feita de uma vez.\n");
        }

        txtResultado.setText(resultado.toString());
    }

    // Adiciona o módulo na lista (sem repetir) e escreve o motivo no texto
    private void registrar(List<String> modulos, StringBuilder detalhes,
                           String problema, String modulo, String motivo) {

        if (!modulos.contains(modulo)) {
            modulos.add(modulo);
        }

        detalhes.append("\n* Problema: ").append(problema).append("\n");
        detalhes.append("  Módulo: ").append(modulo).append("\n");
        detalhes.append("  Motivo: ").append(modulo).append(" ").append(motivo).append("\n");
    }

    // ----------------------------------
    // Início do programa
    // ----------------------------------

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}