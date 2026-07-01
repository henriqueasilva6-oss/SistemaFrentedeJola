package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.ItemVenda;
import model.Produto;
import model.Venda;
import service.Caixa;

public class TelaPDV extends JFrame {

    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextField txtPreco;
    private JTextField txtEstoque;
    private JTextField txtBusca;

    private JTextField txtCodigoVenda;
    private JTextField txtQuantidadeVenda;
    private JTextField txtTotal;
    private JTextField txtValorPago;
    private JTextField txtTroco;

    private JButton btnCadastrar;
    private JButton btnListar;
    private JButton btnBuscar;
    private JButton btnAdicionarCarrinho;
    private JButton btnFinalizarVenda;
    private JButton btnNovaVenda;
    private JButton btnCancelarVenda;

    private JTable tabelaProdutos;
    private JTable tabelaCarrinho;

    private DefaultTableModel modeloProdutos;
    private DefaultTableModel modeloCarrinho;

    private ArrayList<Produto> produtos;
    private Venda vendaAtual;
    private Caixa caixa;

    public TelaPDV() {
        setTitle("Painel de Vendas");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        produtos = new ArrayList<>();
        vendaAtual = new Venda();
        caixa = new Caixa();

        add(criarPainelCadastro(), BorderLayout.NORTH);
        add(criarPainelCentral(), BorderLayout.CENTER);
        add(criarPainelFinanceiro(), BorderLayout.SOUTH);
    }

    private JPanel criarPainelCadastro() {
        JPanel painelCadastro = new JPanel(new GridLayout(5, 2, 10, 10));
        painelCadastro.setBorder(BorderFactory.createTitledBorder("Cadastro de Produtos"));

        painelCadastro.add(new JLabel("Codigo do Produto:"));
        txtCodigo = new JTextField();
        painelCadastro.add(txtCodigo);

        painelCadastro.add(new JLabel("Produto:"));
        txtNome = new JTextField();
        painelCadastro.add(txtNome);

        painelCadastro.add(new JLabel("Preco:"));
        txtPreco = new JTextField();
        painelCadastro.add(txtPreco);

        painelCadastro.add(new JLabel("Quantidade em Estoque:"));
        txtEstoque = new JTextField();
        painelCadastro.add(txtEstoque);

        btnCadastrar = new JButton("Cadastrar Produto");
        painelCadastro.add(btnCadastrar);

        btnListar = new JButton("Listar Produtos");
        painelCadastro.add(btnListar);

        btnCadastrar.addActionListener(e -> cadastrarProduto());
        btnListar.addActionListener(e -> listarProdutos());

        return painelCadastro;
    }

    private JPanel criarPainelCentral() {
        JPanel painelCentral = new JPanel(new GridLayout(1, 2, 10, 10));
        painelCentral.add(criarPainelProdutos());
        painelCentral.add(criarPainelCarrinho());
        return painelCentral;
    }

    private JPanel criarPainelProdutos() {
        JPanel painelProdutos = new JPanel(new BorderLayout(5, 5));
        painelProdutos.setBorder(BorderFactory.createTitledBorder("Produtos"));

        JPanel painelBusca = new JPanel(new BorderLayout(5, 5));

        txtBusca = new JTextField();
        btnBuscar = new JButton("Buscar");

        painelBusca.add(new JLabel("Nome ou codigo:"), BorderLayout.WEST);
        painelBusca.add(txtBusca, BorderLayout.CENTER);
        painelBusca.add(btnBuscar, BorderLayout.EAST);

        modeloProdutos = criarModeloTabela("Codigo", "Produto", "Preco", "Estoque");
        tabelaProdutos = new JTable(modeloProdutos);
        centralizarTabela(tabelaProdutos);

        painelProdutos.add(painelBusca, BorderLayout.NORTH);
        painelProdutos.add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> buscarProdutos());

        return painelProdutos;
    }

    private JPanel criarPainelCarrinho() {
        JPanel painelCarrinho = new JPanel(new BorderLayout(5, 5));
        painelCarrinho.setBorder(BorderFactory.createTitledBorder("Carrinho de Compras"));

        JPanel painelAdicionar = new JPanel(new GridLayout(2, 3, 5, 5));

        painelAdicionar.add(new JLabel("Codigo:"));
        painelAdicionar.add(new JLabel("Quantidade:"));
        painelAdicionar.add(new JLabel(""));

        txtCodigoVenda = new JTextField();
        txtQuantidadeVenda = new JTextField();
        btnAdicionarCarrinho = new JButton("Adicionar");

        painelAdicionar.add(txtCodigoVenda);
        painelAdicionar.add(txtQuantidadeVenda);
        painelAdicionar.add(btnAdicionarCarrinho);

        modeloCarrinho = criarModeloTabela("Codigo", "Produto", "Qtd", "Preco", "Subtotal");
        tabelaCarrinho = new JTable(modeloCarrinho);
        centralizarTabela(tabelaCarrinho);

        painelCarrinho.add(painelAdicionar, BorderLayout.NORTH);
        painelCarrinho.add(new JScrollPane(tabelaCarrinho), BorderLayout.CENTER);

        btnAdicionarCarrinho.addActionListener(e -> adicionarAoCarrinho());

        return painelCarrinho;
    }

    private JPanel criarPainelFinanceiro() {
        JPanel painelFinanceiro = new JPanel(new BorderLayout(10, 10));
        painelFinanceiro.setBorder(BorderFactory.createTitledBorder("Financeiro"));

        JPanel campos = new JPanel(new GridLayout(2, 3, 10, 5));

        campos.add(new JLabel("Total:"));
        campos.add(new JLabel("Valor Pago:"));
        campos.add(new JLabel("Troco:"));

        txtTotal = new JTextField("0.00");
        txtValorPago = new JTextField();
        txtTroco = new JTextField("0.00");

        txtTotal.setEditable(false);
        txtTroco.setEditable(false);

        campos.add(txtTotal);
        campos.add(txtValorPago);
        campos.add(txtTroco);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnNovaVenda = new JButton("Nova Venda");
        btnFinalizarVenda = new JButton("Finalizar Venda");
        btnCancelarVenda = new JButton("Cancelar Venda");

        botoes.add(btnNovaVenda);
        botoes.add(btnFinalizarVenda);
        botoes.add(btnCancelarVenda);

        painelFinanceiro.add(campos, BorderLayout.CENTER);
        painelFinanceiro.add(botoes, BorderLayout.SOUTH);

        btnNovaVenda.addActionListener(e -> novaVenda());
        btnFinalizarVenda.addActionListener(e -> finalizarVenda());
        btnCancelarVenda.addActionListener(e -> cancelarVenda());

        return painelFinanceiro;
    }

    private DefaultTableModel criarModeloTabela(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void centralizarTabela(JTable tabela) {
        DefaultTableCellRenderer renderizadorCentral = new DefaultTableCellRenderer();
        renderizadorCentral.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(renderizadorCentral);
        }
    }

    private void cadastrarProduto() {
        try {
            int codigo = Integer.parseInt(txtCodigo.getText().trim());
            String nome = txtNome.getText().trim();
            double preco = lerValor(txtPreco);
            int estoque = Integer.parseInt(txtEstoque.getText().trim());

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o nome do produto.");
                return;
            }

            if (preco <= 0 || estoque < 0) {
                JOptionPane.showMessageDialog(this, "Preco e estoque devem ser validos.");
                return;
            }

            if (buscarProdutoPorCodigo(codigo) != null) {
                JOptionPane.showMessageDialog(this, "Ja existe um produto com esse codigo.");
                return;
            }

            Produto produto = new Produto(codigo, nome, preco, estoque);
            produtos.add(produto);

            listarProdutos();
            limparCamposProduto();

            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
        } catch (NumberFormatException erro) {
            JOptionPane.showMessageDialog(this, "Preencha codigo, preco e estoque corretamente.");
        }
    }

    private void listarProdutos() {
        modeloProdutos.setRowCount(0);

        for (Produto produto : produtos) {
            adicionarProdutoNaTabela(produto);
        }
    }

    private void buscarProdutos() {
        String termoBusca = txtBusca.getText().trim().toLowerCase();

        if (termoBusca.isEmpty()) {
            listarProdutos();
            return;
        }

        modeloProdutos.setRowCount(0);

        for (Produto produto : produtos) {
            String codigo = String.valueOf(produto.getCodigo());
            String nome = produto.getNome().toLowerCase();

            if (codigo.equals(termoBusca) || nome.contains(termoBusca)) {
                adicionarProdutoNaTabela(produto);
            }
        }
    }

    private void adicionarAoCarrinho() {
        try {
            int codigo = Integer.parseInt(txtCodigoVenda.getText().trim());
            int quantidade = Integer.parseInt(txtQuantidadeVenda.getText().trim());

            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.");
                return;
            }

            Produto produto = buscarProdutoPorCodigo(codigo);

            if (produto == null) {
                JOptionPane.showMessageDialog(this, "Produto nao encontrado.");
                return;
            }

            int quantidadeNoCarrinho = calcularQuantidadeNoCarrinho(produto);

            if (quantidade + quantidadeNoCarrinho > produto.getEstoque()) {
                JOptionPane.showMessageDialog(this, "Estoque insuficiente para esse produto.");
                return;
            }

            ItemVenda item = new ItemVenda(produto, quantidade);
            vendaAtual.adicionarItem(item);

            adicionarItemNaTabelaCarrinho(item);
            atualizarTotal();

            txtCodigoVenda.setText("");
            txtQuantidadeVenda.setText("");
            txtCodigoVenda.requestFocus();
        } catch (NumberFormatException erro) {
            JOptionPane.showMessageDialog(this, "Informe codigo e quantidade corretamente.");
        }
    }

    private void finalizarVenda() {
        if (vendaAtual.getItens().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione produtos ao carrinho antes de finalizar.");
            return;
        }

        try {
            double total = vendaAtual.calcularTotal();
            double valorPago = lerValor(txtValorPago);

            if (valorPago < total) {
                JOptionPane.showMessageDialog(this, "Valor pago insuficiente.");
                return;
            }

            for (ItemVenda item : vendaAtual.getItens()) {
                item.getProduto().baixarEstoque(item.getQuantidade());
            }

            double troco = caixa.calcularTroco(valorPago, total);
            txtTroco.setText(formatarValor(troco));

            listarProdutos();

            JOptionPane.showMessageDialog(this, "Venda finalizada com sucesso!");
            novaVenda();
        } catch (NumberFormatException erro) {
            JOptionPane.showMessageDialog(this, "Informe o valor pago corretamente.");
        }
    }

    private void novaVenda() {
        vendaAtual = new Venda();
        modeloCarrinho.setRowCount(0);
        txtCodigoVenda.setText("");
        txtQuantidadeVenda.setText("");
        txtTotal.setText("0.00");
        txtValorPago.setText("");
        txtTroco.setText("0.00");
    }

    private void cancelarVenda() {
        novaVenda();
        JOptionPane.showMessageDialog(this, "Venda cancelada.");
    }

    private Produto buscarProdutoPorCodigo(int codigo) {
        for (Produto produto : produtos) {
            if (produto.getCodigo() == codigo) {
                return produto;
            }
        }

        return null;
    }

    private int calcularQuantidadeNoCarrinho(Produto produto) {
        int quantidade = 0;

        for (ItemVenda item : vendaAtual.getItens()) {
            if (item.getProduto().getCodigo() == produto.getCodigo()) {
                quantidade += item.getQuantidade();
            }
        }

        return quantidade;
    }

    private void adicionarProdutoNaTabela(Produto produto) {
        modeloProdutos.addRow(new Object[] {
            produto.getCodigo(),
            produto.getNome(),
            formatarValor(produto.getPreco()),
            produto.getEstoque()
        });
    }

    private void adicionarItemNaTabelaCarrinho(ItemVenda item) {
        Produto produto = item.getProduto();

        modeloCarrinho.addRow(new Object[] {
            produto.getCodigo(),
            produto.getNome(),
            item.getQuantidade(),
            formatarValor(produto.getPreco()),
            formatarValor(item.getSubtotal())
        });
    }

    private void atualizarTotal() {
        txtTotal.setText(formatarValor(vendaAtual.calcularTotal()));
    }

    private double lerValor(JTextField campo) {
        return Double.parseDouble(campo.getText().trim().replace(",", "."));
    }

    private String formatarValor(double valor) {
        return String.format("%.2f", valor);
    }

    private void limparCamposProduto() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtPreco.setText("");
        txtEstoque.setText("");
    }
}
