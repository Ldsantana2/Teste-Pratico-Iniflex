import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

        List<Funcionario> funcionarios = new ArrayList<>(Arrays.asList(
            new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"),
            new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"),
            new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"),
            new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"),
            new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"),
            new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"),
            new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"),
            new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"),
            new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"),
            new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente")
        ));

        funcionarios.removeIf(f -> f.getNome().equalsIgnoreCase("João"));

        System.out.println("=== LISTA DE FUNCIONÁRIOS ===");
        imprimirFuncionarios(funcionarios, dateFormatter, currencyFormat);

        funcionarios.forEach(f -> f.setSalario(f.getSalario().multiply(new BigDecimal("1.10"))));

        Map<String, List<Funcionario>> funcionariosPorFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));

        System.out.println("\n=== FUNCIONÁRIOS AGRUPADOS POR FUNÇÃO ===");
        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println("\nFunção: " + funcao);
            imprimirFuncionarios(lista, dateFormatter, currencyFormat);
        });

        System.out.println("\n=== ANIVERSARIANTES DOS MESES 10 E 12 ===");
        funcionarios.stream()
                .filter(f -> f.getDataNascimento().getMonthValue() == 10 || f.getDataNascimento().getMonthValue() == 12)
                .forEach(f -> System.out.println(f.getNome() + " - Data de Nasc.: " + f.getDataNascimento().format(dateFormatter)));

        System.out.println("\n=== FUNCIONÁRIO COM A MAIOR IDADE ===");
        Funcionario maisVelho = Collections.min(funcionarios, Comparator.comparing(Funcionario::getDataNascimento));
        int idade = Period.between(maisVelho.getDataNascimento(), LocalDate.now()).getYears();
        System.out.println("Nome: " + maisVelho.getNome() + " | Idade: " + idade + " anos");


        System.out.println("\n=== FUNCIONÁRIOS EM ORDEM ALFABÉTICA ===");
        List<Funcionario> funcionariosOrdenados = funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .collect(Collectors.toList());
        imprimirFuncionarios(funcionariosOrdenados, dateFormatter, currencyFormat);


        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("\n=== TOTAL DOS SALÁRIOS ===");
        System.out.println("Total: " + currencyFormat.format(totalSalarios));


        BigDecimal salarioMinimo = new BigDecimal("1212.00");
        System.out.println("\n=== QTD DE SALÁRIOS MÍNIMOS POR FUNCIONÁRIO ===");
        funcionarios.forEach(f -> {
            BigDecimal qtdSalariosMinimos = f.getSalario().divide(salarioMinimo, 2, RoundingMode.HALF_UP);
            System.out.println(f.getNome() + " ganha " + qtdSalariosMinimos + " salários mínimos.");
        });
    }

    private static void imprimirFuncionarios(List<Funcionario> lista, DateTimeFormatter df, NumberFormat nf) {
        for (Funcionario f : lista) {
            System.out.println(String.format("Nome: %-8s | Data Nasc: %s | Salário: %-12s | Função: %s",
                    f.getNome(),
                    f.getDataNascimento().format(df),
                    nf.format(f.getSalario()),
                    f.getFuncao()));
        }
    }
}