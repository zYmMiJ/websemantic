(TeX-add-style-hook
 "20180311"
 (lambda ()
   (setq TeX-command-extra-options
         "--shell-escape")
   (TeX-run-style-hooks
    "latex2e"
    "article"
    "art10"
    "pgf"
    "tikz"
    "pgfplots"))
 :latex)

