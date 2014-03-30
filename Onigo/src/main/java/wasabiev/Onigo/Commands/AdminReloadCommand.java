package wasabiev.Onigo.Commands;

public class AdminReloadCommand extends BaseCommand {

	public AdminReloadCommand(){
		bePlayer = false;
		name = "reload";
		argLength = 0;
		usage = "<- reload config.yml";
	}

	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public boolean permission() {
		return sender.hasPermission("onigo.admin.reload");
	}
}
