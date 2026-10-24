package controlefinanceiro.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import controlefinanceiro.model.ResumoFinanceiro;
import controlefinanceiro.util.SessaoUsuario;

public class TelaGrafico extends JFrame {

    public TelaGrafico(ResumoFinanceiro resumo, String periodo) {
        setTitle("Resumo Financeiro - " + SessaoUsuario.getUsuarioLogado() + " (" + periodo + ")");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // fecha só o gráfico, não o app

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(resumo.totalEntradas(), "Entradas", "Resumo");
        dataset.addValue(resumo.totalSaidas(), "Saídas", "Resumo");

        JFreeChart chart = ChartFactory.createBarChart(
                "Resumo Financeiro - " + periodo, "Tipo", "Valor (R$)", dataset);

        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 20));

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 14));
        plot.getRangeAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 14));
        plot.getDomainAxis().setLabelFont(new Font("Arial", Font.BOLD, 14));
        plot.getRangeAxis().setLabelFont(new Font("Arial", Font.BOLD, 14));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(76, 175, 80));  // verde
        renderer.setSeriesPaint(1, new Color(244, 67, 54));  // vermelho
        renderer.setItemMargin(0.30);
        renderer.setMaximumBarWidth(0.08);

        ChartPanel panel = new ChartPanel(chart);
        panel.setBackground(Color.WHITE);
        panel.setMouseWheelEnabled(true);
        setContentPane(panel);
    }
}
