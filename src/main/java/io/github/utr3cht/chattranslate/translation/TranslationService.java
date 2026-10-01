package io.github.utr3cht.chattranslate.translation;

import java.util.concurrent.CompletableFuture;

public interface TranslationService {
    CompletableFuture<String> translate(String text, String targetLang);
}
