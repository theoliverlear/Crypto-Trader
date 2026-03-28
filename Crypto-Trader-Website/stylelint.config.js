export default {
    extends: ['stylelint-config-standard-scss'],
    rules: {
        'scss/at-rule-no-unknown': true,
        'rule-empty-line-before': [
            'always',
            {
                except: ['first-nested'],
                ignore: ['after-comment'],
            },
        ],
        'at-rule-empty-line-before': [
            'always',
            {
                except: ['blockless-after-same-name-blockless', 'first-nested'],
                ignore: ['after-comment'],
            },
        ],
        'declaration-empty-line-before': 'never',
        'selector-type-no-unknown': [false],
        'scss/at-extend-no-missing-placeholder': null,
    },
};
