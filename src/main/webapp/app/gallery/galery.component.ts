import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'jhi-gallery',
  templateUrl: './gallery.component.html',
  styleUrls: ['./gallery.component.scss'],
})
export class GalleryComponent implements OnInit {
  currentIndex: number = 0;
  @Input() images: string[] = [];
  @Input() visible = false;
  @Output() close = new EventEmitter<void>();

  public showGallery: boolean = false;

  constructor() {}

  ngOnInit(): void {}

  get currentImageUrl(): string {
    return this.images[this.currentIndex];
  }

  previousImage(): void {
    if (this.currentIndex > 0) {
      this.currentIndex--;
    }
  }

  nextImage(): void {
    if (this.currentIndex < this.images.length - 1) {
      this.currentIndex++;
    }
  }

  public toggleGallery(): void {
    this.showGallery = !this.showGallery;
  }

  closeGallery(): void {
    this.showGallery = false;
    this.close.emit();
  }
}
