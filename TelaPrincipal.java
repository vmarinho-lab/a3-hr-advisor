import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// =====================================================================
// TELA DO CONSULTOR SAP (também é a classe que inicia o programa)
// =====================================================================

public class TelaPrincipal extends JFrame {

    // Dados da empresa
    private JTextField txtEmpresa;
    private JTextField txtFuncionarios;
    private JComboBox<String> cmbModelo;
    private JTextField txtUnidades;
    private JTextField txtEstados;

    // Problemas de RH
    private JCheckBox chkDados;
    private JCheckBox chkRecrutamento;
    private JCheckBox chkAdmissao;
    private JCheckBox chkFerias;
    private JCheckBox chkDesligamento;
    private JCheckBox chkVisaoGeral;

    // Resultado
    private JTextArea txtResultado;
    private JButton btnRecomendar;
    private JButton btnSair;

    public TelaPrincipal() {

        setTitle("HR Digital Transformation Advisor - Consultor SAP");
        setSize(950, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ----------------------------------
        // Painel Superior: dados da empresa
        // ----------------------------------

        JPanel dados = new JPanel(new GridLayout(5, 2, 5, 5));
        dados.setBorder(BorderFactory.createTitledBorder("Dados da empresa"));

        dados.add(new JLabel("Nome da empresa:"));
        txtEmpresa = new JTextField();
        dados.add(txtEmpresa);

        dados.add(new JLabel("Quantidade de funcionários:"));
        txtFuncionarios = new JTextField();
        dados.add(txtFuncionarios);

        dados.add(new JLabel("Modelo de negócio:"));
        String[] modelos = {
                "Administração de shopping centers",
                "Varejo",
                "Serviços",
                "Indústria",
                "Outro"
        };
        cmbModelo = new JComboBox<>(modelos);
        dados.add(cmbModelo);

        dados.add(new JLabel("Quantidade de unidades:"));
        txtUnidades = new JTextField();
        dados.add(txtUnidades);

        dados.add(new JLabel("Estados onde atua (ex.: SP, RJ, MG):"));
        txtEstados = new JTextField();
        dados.add(txtEstados);

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
        // Botões
        // ----------------------------------

        JPanel botoes = new JPanel(new GridLayout(1, 2, 10, 10));

        btnRecomendar = new JButton("Gerar relatório do consultor SAP SuccessFactors");
        btnRecomendar.addActionListener((ActionEvent e) -> gerarRecomendacao());
        botoes.add(btnRecomendar);

        btnSair = new JButton("Sair (voltar ao login)");
        btnSair.addActionListener((ActionEvent e) -> {
            dispose();
            new TelaLogin().setVisible(true);
        });
        botoes.add(btnSair);

        painel.add(botoes, BorderLayout.SOUTH);

        add(painel);
    }

    // ----------------------------------
    // Lógica: lê os dados e monta o relatório
    // ----------------------------------

    private void gerarRecomendacao() {

        String empresa = txtEmpresa.getText().trim();
        int funcionarios;
        int unidades;

        if (empresa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome da empresa.");
            return;
        }

        try {
            funcionarios = Integer.parseInt(txtFuncionarios.getText().trim());
            unidades = Integer.parseInt(txtUnidades.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Digite a quantidade de funcionários e de unidades usando apenas números.");
            return;
        }

        if (funcionarios <= 0 || unidades <= 0) {
            JOptionPane.showMessageDialog(this,
                    "A quantidade de funcionários e de unidades deve ser maior que zero.");
            return;
        }

        List<String> estados = lerEstados(txtEstados.getText());

        if (estados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe pelo menos um estado (ex.: SP).");
            return;
        }

        String modelo = (String) cmbModelo.getSelectedItem();

        // ----- Escolha dos módulos -----

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

        // ----- Montagem do relatório -----

        String licencasTexto = montarLicencas(modulos, funcionarios, unidades);

        StringBuilder r = new StringBuilder();

        r.append("RELATÓRIO DO CONSULTOR SAP SUCCESSFACTORS\n");
        r.append("Para: Diretor(a) de RH - ").append(empresa).append("\n\n");

        r.append("PERFIL DA EMPRESA\n");
        r.append(" - Funcionários: ").append(funcionarios).append("\n");
        r.append(" - Modelo de negócio: ").append(modelo).append("\n");
        r.append(" - Unidades: ").append(unidades).append("\n");
        r.append(" - Estados (").append(estados.size()).append("): ")
         .append(String.join(", ", estados)).append("\n\n");

        r.append("1) MÓDULOS RECOMENDADOS\n");
        for (String modulo : modulos) {
            r.append(" - ").append(modulo).append("\n");
        }

        r.append("\n2) POR QUE CADA UM\n");
        r.append(detalhes);

        r.append("\n3) ETAPAS DE IMPLANTAÇÃO SUGERIDAS\n");
        r.append(montarFases(modulos, unidades));

        r.append("\n4) ESTIMATIVA DE LICENÇAS\n");
        r.append(licencasTexto);

        r.append("\n5) CAPACITAÇÃO RECOMENDADA\n");
        r.append(" - Para todos: trilhas-base do SAP Learning sobre soluções SAP para gestão de talentos ")
         .append("e ciclo de vida do colaborador no SuccessFactors.\n");
        for (String modulo : modulos) {
            r.append(" - ").append(modulo).append(": ")
             .append(capacitacaoDoModulo(modulo)).append("\n");
        }

        r.append("\n6) OBSERVAÇÕES\n");
        r.append(montarObservacoes(modulos, funcionarios, estados.size(), modelo));

        txtResultado.setText(r.toString());
        txtResultado.setCaretPosition(0);

        // ----- Guarda os dados para as outras telas (licenças, administrador, RH) -----

        DadosCase.empresa = empresa;
        DadosCase.funcionarios = funcionarios;
        DadosCase.unidades = unidades;
        DadosCase.estados = String.join(", ", estados);
        DadosCase.modulos = new ArrayList<>(modulos);
        DadosCase.licencas = licencasTexto;
        DadosCase.relatorio = r.toString();
        DadosCase.licencasLiberadas = false;
        DadosCase.liberadoPor = "";

        DadosCase.registrarAcao("Consultor gerou o relatório para " + empresa);
    }

    // Lê os estados digitados (separados por vírgula, ponto e vírgula ou barra)
    private List<String> lerEstados(String texto) {
        List<String> estados = new ArrayList<>();

        for (String parte : texto.split("[,;/]")) {
            String estado = parte.trim().toUpperCase();
            if (!estado.isEmpty() && !estados.contains(estado)) {
                estados.add(estado);
            }
        }

        return estados;
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

    // Sugere a ordem de implantação dos módulos recomendados
    private String montarFases(List<String> modulos, int unidades) {

        StringBuilder fases = new StringBuilder();
        int numero = 1;

        if (modulos.contains("Employee Central")) {
            fases.append(" - Etapa ").append(numero++)
                 .append(": Employee Central (base com os dados dos colaboradores)\n");
        }

        List<String> entrada = new ArrayList<>();
        if (modulos.contains("Recruiting Management")) {
            entrada.add("Recruiting Management");
        }
        if (modulos.contains("Onboarding 2.0")) {
            entrada.add("Onboarding 2.0");
        }
        if (!entrada.isEmpty()) {
            fases.append(" - Etapa ").append(numero++).append(": ")
                 .append(String.join(" e ", entrada))
                 .append(" (ciclo de entrada e saída de pessoas)\n");
        }

        if (modulos.contains("Time Off")) {
            fases.append(" - Etapa ").append(numero++)
                 .append(": Time Off (rotina de férias e ausências)\n");
        }

        if (!modulos.contains("Employee Central")) {
            fases.append(" - Atenção: considere incluir o Employee Central, ")
                 .append("que guarda os dados dos colaboradores usados pelos outros módulos.\n");
        }

        if (unidades > 1) {
            fases.append(" - Em cada etapa, começar por 1 unidade piloto e depois expandir para as outras ")
                 .append(unidades - 1).append(" unidades.\n");
        }

        return fases.toString();
    }

    // Estimativa didática de licenças (hipótese do case, não é regra comercial da SAP)
    private String montarLicencas(List<String> modulos, int funcionarios, int unidades) {

        StringBuilder licencas = new StringBuilder();

        for (String modulo : modulos) {
            if (modulo.equals("Recruiting Management")) {
                licencas.append(" - ").append(modulo).append(": cerca de ").append(unidades)
                        .append(" (1 recrutador por unidade)\n");
            } else {
                licencas.append(" - ").append(modulo).append(": ").append(funcionarios)
                        .append(" (1 por colaborador)\n");
            }
        }

        licencas.append(" Estimativa didática feita para este case, sem relação com preço ou regra comercial da SAP.\n");

        return licencas.toString();
    }

    // Quem precisa ser treinado em cada módulo e em quê
    private String capacitacaoDoModulo(String modulo) {

        if (modulo.equals("Employee Central")) {
            return "time de RH: cadastro e manutenção dos dados dos colaboradores; gestores: consulta à equipe.";
        }
        if (modulo.equals("Recruiting Management")) {
            return "recrutadores e gestores das unidades: abertura de vagas, acompanhamento de candidatos e processo seletivo padronizado.";
        }
        if (modulo.equals("Onboarding 2.0")) {
            return "time de RH e gestores: acompanhar admissão, movimentação interna e desligamento; novos colaboradores: documentos e assinatura digital.";
        }
        if (modulo.equals("Time Off")) {
            return "todos os colaboradores: pedir férias e ausências; gestores: aprovar pedidos; RH: configurar regras e calendários.";
        }

        return "treinamento básico do módulo.";
    }

    // Observações que dependem do porte, dos estados e do modelo de negócio
    private String montarObservacoes(List<String> modulos, int funcionarios,
                                     int totalEstados, String modelo) {

        StringBuilder obs = new StringBuilder();

        if (funcionarios >= 1000) {
            obs.append(" - Com ").append(funcionarios)
               .append(" funcionários, recomenda-se implantar em fases e ter uma equipe de gestão da mudança.\n");
        } else {
            obs.append(" - Por ser uma empresa de menor porte, a implantação pode ser mais rápida.\n");
        }

        if (totalEstados > 1 && modulos.contains("Time Off")) {
            obs.append(" - Como a empresa atua em ").append(totalEstados)
               .append(" estados, configurar calendários de feriados e regras de ausência por localidade.\n");
        }

        if (modelo.equals("Administração de shopping centers") || modelo.equals("Varejo")) {
            obs.append(" - Equipes operacionais com turnos e rotatividade tendem a se beneficiar de admissão digital e controle de ausências.\n");
        }

        return obs.toString();
    }

    // ----------------------------------
    // Início do programa: abre a tela de login
    // ----------------------------------

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}

// =====================================================================
// DADOS COMPARTILHADOS ENTRE AS TELAS (simula o "banco de dados")
// =====================================================================

class DadosCase {

    static String empresa = "";
    static int funcionarios = 0;
    static int unidades = 0;
    static String estados = "";
    static List<String> modulos = new ArrayList<>();
    static String licencas = "";
    static String relatorio = "";

    static boolean licencasLiberadas = false;
    static String liberadoPor = "";

    // Controle de acessos: guarda quem entrou e o que fez
    static List<String> registroAcessos = new ArrayList<>();

    static boolean temRelatorio() {
        return !relatorio.isEmpty();
    }

    static void registrarAcao(String texto) {
        String hora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        registroAcessos.add(hora + " - " + texto);
    }

    // Texto da tela do gestor de licenças
    static String textoLicencas() {

        if (!temRelatorio()) {
            return "O consultor ainda não gerou uma recomendação.\n"
                 + "Entre como consultor, gere o relatório, saia e volte aqui.";
        }

        StringBuilder s = new StringBuilder();
        s.append("Empresa: ").append(empresa).append("\n");
        s.append("Funcionários: ").append(funcionarios).append("\n");
        s.append("Unidades: ").append(unidades).append("\n\n");
        s.append("ESTIMATIVA DE LICENÇAS\n").append(licencas).append("\n");
        s.append("SITUAÇÃO: ");

        if (licencasLiberadas) {
            s.append("licenças liberadas por ").append(liberadoPor).append(".");
        } else {
            s.append("aguardando liberação.");
        }

        return s.toString();
    }

    // Texto da tela do administrador (visão gerencial)
    static String textoGerencial() {

        StringBuilder s = new StringBuilder();
        s.append("VISÃO GERENCIAL\n\n");

        if (!temRelatorio()) {
            s.append("Nenhuma recomendação foi gerada ainda.\n");
        } else {
            s.append("Empresa: ").append(empresa).append("\n");
            s.append("Funcionários: ").append(funcionarios).append("\n");
            s.append("Unidades: ").append(unidades).append("\n");
            s.append("Estados: ").append(estados).append("\n");
            s.append("Módulos recomendados: ").append(String.join(", ", modulos)).append("\n");
            s.append("Licenças: ");
            if (licencasLiberadas) {
                s.append("liberadas por ").append(liberadoPor).append("\n");
            } else {
                s.append("aguardando liberação\n");
            }
        }

        s.append("\nREGISTRO DE ACESSOS E AÇÕES\n");

        if (registroAcessos.isEmpty()) {
            s.append(" (nenhum registro)\n");
        }
        for (String linha : registroAcessos) {
            s.append(" - ").append(linha).append("\n");
        }

        return s.toString();
    }

    // Texto da tela do usuário do RH (somente consulta)
    static String textoUsuarioRH() {

        if (!temRelatorio()) {
            return "Ainda não há recomendação disponível.\n"
                 + "O consultor precisa gerar o relatório primeiro.";
        }

        return "MODO CONSULTA (somente leitura)\n\n" + relatorio;
    }
}

// =====================================================================
// TELA DE LOGIN: cada usuário abre a tela do seu perfil
// =====================================================================

class TelaLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtSenha;

    TelaLogin() {

        setTitle("HR Digital Transformation Advisor - Login");
        setSize(560, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Acesso ao sistema", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        painel.add(titulo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.add(new JLabel("Usuário:"));
        txtUsuario = new JTextField();
        form.add(txtUsuario);
        form.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        form.add(txtSenha);

        JTextArea demo = new JTextArea(
                "Usuários de demonstração (só para a apresentação):\n\n"
              + " consultor / consultor123  ->  Consultor SAP\n"
              + " licencas / licencas123  ->  Gestor de licenças\n"
              + " admin / admin123  ->  Administrador (visão gerencial)\n"
              + " rh / rh123  ->  Usuário do RH da empresa");
        demo.setEditable(false);
        demo.setOpaque(false);
        demo.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel centro = new JPanel(new BorderLayout(5, 15));
        centro.add(form, BorderLayout.NORTH);
        centro.add(demo, BorderLayout.CENTER);
        painel.add(centro, BorderLayout.CENTER);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener((ActionEvent e) -> entrar());
        txtSenha.addActionListener((ActionEvent e) -> entrar());

        JLabel versao = new JLabel("Versão 1.0", SwingConstants.RIGHT);

        JPanel sul = new JPanel(new BorderLayout(5, 5));
        sul.add(btnEntrar, BorderLayout.CENTER);
        sul.add(versao, BorderLayout.SOUTH);
        painel.add(sul, BorderLayout.SOUTH);

        add(painel);
    }

    private void entrar() {

        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword());

        String perfil = descobrirPerfil(usuario, senha);

        if (perfil == null) {
            DadosCase.registrarAcao("Tentativa de acesso negada para o usuário '" + usuario + "'");
            JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos.");
            return;
        }

        DadosCase.registrarAcao(usuario + " entrou como " + perfil);
        dispose();

        if (perfil.equals("Consultor SAP")) {
            new TelaPrincipal().setVisible(true);
        } else if (perfil.equals("Gestor de licenças")) {
            new TelaLicencas(usuario).setVisible(true);
        } else if (perfil.equals("Administrador")) {
            new TelaAdministrador().setVisible(true);
        } else {
            new TelaUsuarioRH().setVisible(true);
        }
    }

    // Usuários fixos no código (simulação, sem banco de dados)
    private String descobrirPerfil(String usuario, String senha) {

        if (usuario.equals("consultor") && senha.equals("consultor123")) {
            return "Consultor SAP";
        }
        if (usuario.equals("licencas") && senha.equals("licencas123")) {
            return "Gestor de licenças";
        }
        if (usuario.equals("admin") && senha.equals("admin123")) {
            return "Administrador";
        }
        if (usuario.equals("rh") && senha.equals("rh123")) {
            return "Usuário do RH";
        }

        return null;
    }
}

// =====================================================================
// BASE DAS TELAS SIMPLES (título, texto e botão Sair)
// =====================================================================

abstract class TelaBase extends JFrame {

    protected JTextArea txtInfo = new JTextArea();
    protected JPanel botoes = new JPanel(new GridLayout(1, 0, 10, 10));

    TelaBase(String titulo, String perfil) {

        setTitle("HR Digital Transformation Advisor - " + perfil);
        setSize(800, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel(titulo + "  |  Perfil: " + perfil);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(lblTitulo, BorderLayout.NORTH);

        txtInfo.setEditable(false);
        txtInfo.setLineWrap(true);
        txtInfo.setWrapStyleWord(true);
        txtInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        painel.add(new JScrollPane(txtInfo), BorderLayout.CENTER);

        JButton btnSair = new JButton("Sair (voltar ao login)");
        btnSair.addActionListener((ActionEvent e) -> {
            dispose();
            new TelaLogin().setVisible(true);
        });
        botoes.add(btnSair);

        painel.add(botoes, BorderLayout.SOUTH);

        add(painel);
    }
}

// =====================================================================
// TELA DO GESTOR DE LICENÇAS
// =====================================================================

class TelaLicencas extends TelaBase {

    private String usuario;
    private JButton btnLiberar;

    TelaLicencas(String usuario) {

        super("Liberação de licenças", "Gestor de licenças");
        this.usuario = usuario;

        btnLiberar = new JButton("Liberar licenças");
        btnLiberar.addActionListener((ActionEvent e) -> liberar());
        botoes.add(btnLiberar);

        atualizar();
    }

    private void liberar() {
        DadosCase.licencasLiberadas = true;
        DadosCase.liberadoPor = usuario;
        DadosCase.registrarAcao(usuario + " liberou as licenças");
        atualizar();
    }

    private void atualizar() {
        txtInfo.setText(DadosCase.textoLicencas());
        btnLiberar.setEnabled(DadosCase.temRelatorio() && !DadosCase.licencasLiberadas);
    }
}

// =====================================================================
// TELA DO ADMINISTRADOR (visão gerencial)
// =====================================================================

class TelaAdministrador extends TelaBase {

    TelaAdministrador() {

        super("Painel gerencial", "Administrador");

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener((ActionEvent e) -> txtInfo.setText(DadosCase.textoGerencial()));
        botoes.add(btnAtualizar);

        txtInfo.setText(DadosCase.textoGerencial());
    }
}

// =====================================================================
// TELA DO USUÁRIO DO RH (somente consulta)
// =====================================================================

class TelaUsuarioRH extends TelaBase {

    TelaUsuarioRH() {

        super("Consulta da recomendação", "Usuário do RH");

        txtInfo.setText(DadosCase.textoUsuarioRH());
    }
}