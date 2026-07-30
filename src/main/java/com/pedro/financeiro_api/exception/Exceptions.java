package com.pedro.financeiro_api.exception;

public class Exceptions {

    public static class RecursoNaoEncontradoException extends RuntimeException {
        public RecursoNaoEncontradoException(String mensagem) {
            super(mensagem);
        }
    }

    public static class RecursoJaExisteException extends RuntimeException {
        public RecursoJaExisteException(String mensagem) {
            super(mensagem);
        }
    }

    public static class RegraDeNegocioException extends RuntimeException {
        public RegraDeNegocioException(String mensagem) {
            super(mensagem);
        }
    }
}