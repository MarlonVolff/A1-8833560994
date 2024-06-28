import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {
        List<Aluno> listaDeAlunos = new ArrayList<>();

        String arquivoCSV = "alunos.csv";
        String prova;
        boolean primeiraLinha = true;
        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            while ((prova = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] dados = prova.split(";");
                int matricula = Integer.parseInt(dados[0].trim());
                String nome = dados[1].trim();
                double nota = Double.parseDouble(dados[2].replace(",", ".").trim());

                Aluno aluno = new Aluno(matricula, nome, nota);
                listaDeAlunos.add(aluno);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo " + arquivoCSV);
            e.printStackTrace();
            return;
        }

        processarDados(listaDeAlunos);
    }

    public static void processarDados(List<Aluno> listaDeAlunos) {
        int quantidadeAlunos = listaDeAlunos.size();
        int aprovados = 0;
        int reprovados = 0;
        double menorNota = Double.MAX_VALUE;
        double maiorNota = Double.MIN_VALUE;
        double somaDasNotas = 0;

        for (Aluno aluno : listaDeAlunos) {
            double nota = aluno.getNota();
            somaDasNotas += nota;

            if (nota >= 6.0) {
                aprovados++;
            } else {
                reprovados++;
            }

            if (nota < menorNota) {
                menorNota = nota;
            }

            if (nota > maiorNota) {
                maiorNota = nota;
            }
        }

        double mediaGeral = somaDasNotas / quantidadeAlunos;

        gerarArquivoResumo(quantidadeAlunos, aprovados, reprovados, menorNota, maiorNota, mediaGeral);
    }

    // PARA APARECER O RESUMO.CSV
    public static void gerarArquivoResumo(int quantidadeAlunos, int aprovados, int reprovados,
            double menorNota, double maiorNota, double mediaGeral) {
        String arquivoResumo = "resumo.csv";
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(arquivoResumo)))) {
            writer.println("Quantidade de alunos;Aprovados;Reprovados;Menor Nota;Maior Nota;Média Geral");
            writer.printf("%d;%d;%d;%.2f;%.2f;%.2f\n", quantidadeAlunos, aprovados, reprovados,
                    menorNota, maiorNota, mediaGeral);
            System.out.println("Arquivo resumo.csv gerado com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao gerar o arquivo resumo.csv");
            e.printStackTrace();
        }
    }
}
