/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufjf.dcc192;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ice
 */
@WebServlet(name = "JurosCompostosServlet", urlPatterns = {"/index.html"})
public class JurosCompostosServlet extends HttpServlet {
    
    private Double valor;
    private Double taxaDeJuros;
    private Double valorFinal;
    private Integer meses;
    private List<Double> juros;
    private Map<Integer, List<Double>> listaJuros;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String error = "";

        if (request.getParameter("valor") == null || request.getParameter("meses") == null || request.getParameter("taxaDeJuros") == null) {
            this.valor = 5000.0;
            this.meses = 12;
            this.taxaDeJuros = 0.01;
            error = "Um dos parametros foi passado errado, portanto estão nos valores padrão (valor=" + this.valor + ", meses=" + this.meses + ", taxaDeJuros=" + this.taxaDeJuros + ").<br><br>";
        } else {
            try {
                this.valor = Double.parseDouble(request.getParameter("valor"));
                this.meses = Integer.parseInt(request.getParameter("meses"));
                this.taxaDeJuros = Double.parseDouble(request.getParameter("taxaDeJuros"));
            } catch (NumberFormatException | ArithmeticException | ExceptionInInitializerError e) {
                this.valor = 5000.0;
                this.meses = 12;
                this.taxaDeJuros = 0.01;
                error = "Um dos parametros foi passado errado, portanto estão nos valores padrão (valor=" + this.valor + ", meses=" + this.meses + ", taxaDeJuros=" + this.taxaDeJuros + ").<br><br>";
            }
        }
        
        this.juros = new ArrayList<>();
        this.juros.add(0.5);
        this.juros.add(1.0);
        this.juros.add(1.5);
        this.listaJuros = new HashMap<>();

        // valor*(1+12*taxa de juros)
        for (int i = 1; i <= this.meses; i++) {
            this.listaJuros.put(i, new ArrayList<>());
            for (int j = 0; j < this.juros.size(); j++) {
                this.listaJuros.get(i).add(this.valor * Math.pow((1 + this.juros.get(j) * this.taxaDeJuros), i));
            }
        }
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet JurosCompostosServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Juros Compostos Servlet - " + request.getContextPath() + "</h1>");
            
            out.println(error);

            out.println("Exemplo de BOM negócio: ");
            out.println("<a href='http://localhost:8080/dcc192-2018-1-lst1-exr04/?valor=2000&meses=10&taxaDeJuros=0.02'>"
                    + "http://localhost:8080/dcc192-2018-1-lst1-exr04/?valor=2000&meses=10&taxaDeJuros=0.02</a><br><br>");
            out.println("Exemplo de MAU negócio: ");
            out.println("<a href='http://localhost:8080/dcc192-2018-1-lst1-exr04/?valor=2000&meses=10&taxaDeJuros=0.08'>"
                    + "http://localhost:8080/dcc192-2018-1-lst1-exr04/?valor=2000&meses=10&taxaDeJuros=0.08</a><br><br>");

            out.println("<table border='1'>");
            out.println("<thead>");
            out.println("<th>Mês</th>");
            for (Double juro : this.juros) {
                out.println("<th>" + juro + "*" + this.taxaDeJuros + "*" + 100 + " = " + (juro * this.taxaDeJuros * 100) + "%</th>");
            }
            out.println("</thead>");
            out.println("<tbody>");
            this.listaJuros.forEach((mes, arrayJuros) -> {
                out.println("<tr>");
                out.println("<td>" + mes + "</td>");
                for (Double juro : arrayJuros) {
                    out.printf("<td>%.2f</td>\n", juro);
                }
                out.println("</tr>");
            });
            out.println("</tbody>");
            out.println("</table>");
            
            
            Double resultado = this.valor * Math.pow((1 + this.taxaDeJuros), this.meses);
            out.printf("<br>Para um investimento inicial de R$%.2f a uma taxa de juros compostos de %.2f%% ao mês, você terá R$%.2f ao final de %2d meses!", this.valor, (this.taxaDeJuros * 100), resultado, this.meses);
            
            if ((resultado-this.valor)/this.meses >= 200) {
                out.printf("<h3 style='color:green;'>Bom Negócio (R$%.2f)</h3>", (resultado-this.valor)/this.meses);
            } else {
                out.printf("<h3 style='color:red;'>Mau Negócio (R$%.2f)</h3>", (resultado-this.valor)/this.meses);
            }

            
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
