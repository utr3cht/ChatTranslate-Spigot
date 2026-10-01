package io.github.utr3cht.chattranslate.translation;

import java.util.concurrent.CompletableFuture;

public class DummyTranslator implements TranslationService {
    @Override
    public CompletableFuture<String> translate(String text, String targetLang) {
        return CompletableFuture.completedFuture(text);
    }
}
