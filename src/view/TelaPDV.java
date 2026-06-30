package view;
import model.ItemVenda;
import model.Produto;
import model.Venda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaPDV extends JFrame {

    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextField txtPreco;
    private JTextField txtEstoque;
    private JButton btnCadastrar;
    private JButton btnListar;
    private JTable tabelaProdutos;
    private DefaultTableModel listaProdutos;
  
    public TelaPDV() {
        setTitle("Painel de Vendas");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(CadastroProdutos(), BorderLayout.NORTH);
        add(ListarProdutos(), BorderLayout.CENTER);

    
    }

    private JPanel CadastroProdutos() {
        JPanel CadastroProdutos = new JPanel(new GridLayout(5, 2, 10, 10));

        CadastroProdutos.add(new JLabel("Codigo do Produto:"));
        txtCodigo = new JTextField();
        CadastroProdutos.add(txtCodigo);

        CadastroProdutos.add(new JLabel("Nome do Produto:"));
        txtNome = new JTextField();
        CadastroProdutos.add(txtNome);

        CadastroProdutos.add(new JLabel("Preço:"));
        txtPreco = new JTextField();
        CadastroProdutos.add(txtPreco);

        CadastroProdutos.add(new JLabel("Estoque:"));
        txtEstoque = new JTextField();
        CadastroProdutos.add(txtEstoque);

        btnCadastrar = new JButton("Cadastrar Produto");
        CadastroProdutos.add(btnCadastrar);

        btnListar = new JButton("Listar Produtos");
        CadastroProdutos.add(btnListar);

        btnCadastrar.addActionListener(e -> cadastrarProdutos());
        btnListar.addActionListener(e -> listarProdutos());
        

        return CadastroProdutos;
    }

    private JPanel ListarProdutos() {
        JPanel painel = new JPanel (new BorderLayout());

        listaProdutos = new DefaultTableModel();

        listaProdutos.addColumn("Código");
        listaProdutos.addColumn("Produto");
        listaProdutos.addColumn("Preço");
        listaProdutos.addColumn("Estoque");

        tabelaProdutos = new JTable(listaProdutos);

    JScrollPane scroll = new JScrollPane(tabelaProdutos);

    painel.add(scroll, BorderLayout.CENTER);

    return painel;  
    }

    private void cadastrarProdutos() {
    int codigo = Integer.parseInt(txtCodigo.getText());
    String nome = txtNome.getText();
    double preco = Double.parseDouble(txtPreco.getText());
    int estoque = Integer.parseInt(txtEstoque.getText());

    Produto produto = new Produto(codigo, nome, preco, estoque);

    nome.add(itens);

    JOptionPane.showMessageDialog(
            this,
            "Produto cadastrado com sucesso!"
    );

    txtCodigo.setText("");
    txtNome.setText("");
    txtPreco.setText("");
    txtEstoque.setText("");
}


private void listarProdutos() {
    listaProdutos.setRowCount(0);

    for (Produto produto : itemvend) {
        listaProdutos.addRow(new Object[] {
                produto.getCodigo(),
                produto.getNome(),
                produto.getPreco(),
                produto.getEstoque()
        });
    }
}

}