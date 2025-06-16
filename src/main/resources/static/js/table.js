$(document).ready(function () {
    $('#tabela-grid').DataTable({
        language: {
            "decimal":        "",
            "emptyTable":     "Nenhum dado disponível na tabela",
            "info":           "Mostrando _START_ até _END_ de _TOTAL_ registros",
            "infoEmpty":      "Mostrando 0 até 0 de 0 registros",
            "infoFiltered":   "(filtrado de _MAX_ registros no total)",
            "lengthMenu":     "Mostrar _MENU_ registros",
            "loadingRecords": "Carregando...",
            "processing":     "Processando...",
            "search":         "Buscar:",
            "zeroRecords":    "Nenhum registro correspondente encontrado",
            "paginate": {
                "first":      "Primeiro",
                "last":       "Último",
                "next":       "Próximo",
                "previous":   "Anterior"
            },
            "aria": {
                "sortAscending":  ": ativar para ordenar a coluna em ordem crescente",
                "sortDescending": ": ativar para ordenar a coluna em ordem decrescente"
            }
        }
    });
});