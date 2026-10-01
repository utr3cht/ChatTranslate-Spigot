package me.kiva.chattranslate.translation;

import java.util.concurrent.CompletableFuture;

public interface TranslationService {
    CompletableFuture<String> translate(String text, String targetLang);
}
