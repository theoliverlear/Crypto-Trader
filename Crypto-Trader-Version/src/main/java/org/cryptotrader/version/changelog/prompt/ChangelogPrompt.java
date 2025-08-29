package org.cryptotrader.version.changelog.prompt;

public enum ChangelogPrompt {
    SYSTEM("""
                You are an expert release notes writer for a multi-module monorepo. Your job is to produce a Markdown changelog section strictly following the provided template and rules.
                RULES:
                - Output MUST be valid Markdown and MUST NOT include any preface, explanations, or code fences.
                - Do not invent data. Only use the information given in the user prompt (template fragment, release info, commit messages, and optional module list).
                - Keep bullets concise and impactful. Prefer imperative mood (Add, Fix, Update, Remove, Refactor, Docs, Test, Build, CI).
                - Group items by module when asked; otherwise, put repo-wide items in the root [Crypto Trader] section.
                - If a section has no content, omit that section entirely (do not leave placeholder bullets).
                - Keep 3–6 bullets for Highlights when possible. Include notable features, fixes, and breaking changes.
                - Use backticks for code identifiers when appropriate.
                - Never include any content outside of the changelog section you generate.
                - If commit messages are noisy (merge, chore), rewrite succinctly or skip if not relevant to end users.
                OUTPUT:
                - Return ONLY the release section starting from the first heading under "# Changelog" through the end of the sections you create.
                - Refer to the following template for formatting guidelines:
                %s
            """),
    USER("""
            You will generate a new release section that matches the style used by this repository's CHANGELOG.md.
                The versions you may use are:
                
                %s
                
                The release is version: %s. The date is: %s.
                Use the following template as the formatting guide. Follow headings, tables, and link structure exactly; but only include sections that have content.
                
                TEMPLATE (do not echo placeholders literally; fill with real content where possible):
                ## <RELEASE_TAG> – <DATE>
                
                ---
                
                **Full Changelog**: https://github.com/<OWNER>/<REPO>/compare/<FROM>...<TO>
                
                ### Module Version
                
                | Module                 | Version |
                |------------------------|---------|
                | Crypto-Trader          | <VER>   |
                | Crypto-Trader-Admin    | <VER>   |
                | Crypto-Trader-Analysis | <VER>   |
                | Crypto-Trader-Api      | <VER>   |
                | Crypto-Trader-Assets   | <VER>   |
                | Crypto-Trader-Library  | <VER>   |
                | Api-Library            | <VER>   |
                | Api-Communication      | <VER>   |
                | Api-Components         | <VER>   |
                | Api-Models             | <VER>   |
                | Api-Repositories       | <VER>   |
                | Desktop-Library        | <VER>   |
                | Desktop-Components     | <VER>   |
                | Desktop-Styles         | <VER>   |
                | Externals-Library      | <VER>   |
                | Externals-OpenAI       | <VER>   |
                | Version-Library        | <VER>   |
                | Version-Models         | <VER>   |
                | Crypto-Trader-Testing  | <VER>   |
                | Crypto-Trader-Version  | <VER>   |
                | Crypto-Trader-Website  | <VER>   |
                
                ### Breaking changes
                - Bullet(s) if any breaking changes exist.
                
                ### Highlights
                - 3–6 concise bullets summarizing the most important user-facing changes.
                
                ---
                
                ### [Crypto Trader](./)
                - Repo-wide bullets.
                
                ### [Crypto Trader Admin](Crypto-Trader-Admin)
                - Module-specific bullets.
                
                ### [Crypto Trader Analysis](Crypto-Trader-Analysis)
                - Module-specific bullets.
                
                ### [Crypto Trader Api](Crypto-Trader-Api)
                - Module-specific bullets.
                
                (Other module sections follow the same pattern; only include those with content.)
                
                ## Other
                
                ### Repo changes
                - Maintenance / CI / build / docs changes not tied to a single module.
                
                INSTRUCTIONS:
                - Fill in <RELEASE_TAG>, <DATE>, <FROM>, <TO>, and the compare URL if those values are provided below. If any are missing, omit that line gracefully.
                - For the Module Version table: If a concrete version for a module is known (provided below), put it; otherwise omit that row or the entire table if nothing is known. Do not leave placeholder versions.
                - Derive bullets from commit messages below. Aggregate similar commits and remove noise (merge/chore/version-only). Prefer meaningful summaries over raw commit messages.
                - Classify changes into Breaking changes (if clearly indicated), Highlights (top changes), per-module sections, and Other/Repo changes.
                - Do not fabricate modules or features not supported by the commit messages.
                
                CONTEXT:
                - Release tag (if provided): <RELEASE_TAG>
                - Release date (if provided): <DATE>
                - Compare range (if provided): <FROM>..@<TO>
                - Repo path (if provided): <OWNER>/<REPO>
                - Known module versions (if provided):
                <MODULE_VERSIONS>
                
                COMMIT MESSAGES (newest first):
            """);
    
    private final String text;
    
    ChangelogPrompt(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
