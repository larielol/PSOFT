class Radio { 
    private Command playCommand; 
    private Command stopCommand; 
    private Command goBackCommand; 
    private Command goForwardCommand;

    public Radio(Command playCommand, Command stopCommand, Command goBackCommand, Command goForwardCommand) {
        this.playCommand = playCommand;
        this.stopCommand = stopCommand;
        this.goBackCommand = goBackCommand;
        this.goForwardCommand = goForwardCommand;
    }

    public void commandPlay() {
        playCommand.executar();
    }
    
    public void commandStop() {
        stopCommand.executar();
    }
    
    public void commandGoBack() {
        goBackCommand.executar();
    }
    
    public void commandGoForward() {
        goForwardCommand.executar();
    }
}